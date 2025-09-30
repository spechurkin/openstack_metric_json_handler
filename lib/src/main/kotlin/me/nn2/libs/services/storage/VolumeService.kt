package me.nn2.libs.services.storage

import me.nn2.libs.data.storage.VolumeData
import me.nn2.libs.data.storage.VolumeTypeData
import me.nn2.libs.services.IMetricService
import me.nn2.libs.services.compute.ImageService
import org.openstack4j.api.Builders
import org.openstack4j.api.OSClient

class VolumeService(override val client: OSClient.OSClientV3) : IMetricService {
    val imageService = ImageService(client)

    fun getVolume(): List<VolumeData> {
        return convertVolumeToDto()
    }

    fun createVolume(
        volumeName: String,
        description: String?,
        size: Int,
        volumeType: String,
        imageName: String?
    ) {
        val volumeCreate = Builders.volume()
            .name(volumeName)
            .description(description.orEmpty())
            .size(size)
            .volumeType(volumeType)

        if (!imageName.isNullOrEmpty()) {
            volumeCreate.bootable(true)
            volumeCreate.imageRef(imageService.getImageIdByName(imageName))
        }

        client.blockStorage().volumes().create(volumeCreate.build())
    }

    fun updateVolume(volumeName: String, newName: String, description: String, newSize: Int) {
        val volumeId = getVolumeIdByName(volumeName)

        client.blockStorage().volumes().update(volumeId, newName, description)
        if (newSize > 0) {
            client.blockStorage().volumes().extend(volumeId, newSize)
        }
    }

    fun deleteVolume(volumeName: String) {
        val volumeId = getVolumeIdByName(volumeName)

        client.blockStorage().volumes().delete(volumeId)
        client.blockStorage().volumes().forceDelete(volumeId)
    }

    fun getVoluteTypes(): List<VolumeTypeData> {
        return convertVolumeTypeToDto()
    }

    fun createVolumeType(typeName: String, specs: Map<String, String>) {
        client.blockStorage().volumes().createVolumeType(
            Builders.volumeType()
                .name(typeName)
                .extraSpecs(specs)
                .build()
        )
    }

    fun getVolumeIdByName(volumeName: String): String? {
        return client.blockStorage().volumes().list().find { it.name == volumeName }?.id
    }

    private fun convertVolumeTypeToDto(): List<VolumeTypeData> {
        return client.blockStorage().volumes().listVolumeTypes().map {
            VolumeTypeData(
                id = it.id,
                name = it.name,
                specs = it.extraSpecs
            )
        }
    }

    private fun convertVolumeToDto(): List<VolumeData> {
        return client.blockStorage().volumes().list().map {
            VolumeData(
                id = it.id,
                name = it.name,
                description = it.description,
                size = it.size,
                status = it.status,
                metadata = it.metaData.map { (key, value) ->
                    StringBuilder().append(key).append("=").append(value).toString()
                },
                created = it.created
            )
        }
    }
}