package me.nn2.libs.services.storage

import me.nn2.libs.data.storage.VolumeData
import me.nn2.libs.data.storage.VolumeTypeData
import me.nn2.libs.services.AbstractMetricService
import me.nn2.libs.services.compute.ImageService
import org.openstack4j.api.Builders
import org.openstack4j.api.OSClient.OSClientV3
import org.openstack4j.model.storage.block.Volume

class VolumeService(client: OSClientV3) : AbstractMetricService(client) {
    val imageService = ImageService(client)

    fun getVolumes(): List<VolumeData> {
        return convertVolumeToDto()
    }

    fun getVolume(volumeName: String?): Volume? {
        return client.blockStorage().volumes().list().firstOrNull { it.name == volumeName }
    }

    fun createVolume(
        volumeName: String?,
        description: String?,
        size: Int,
        volumeType: String?,
        imageName: String?
    ) {
        if (getVolume(volumeName) == null) {
            val volumeCreate = Builders.volume()
                .name(volumeName)
                .description(description.orEmpty())
                .size(size)
                .volumeType(volumeType)

            if (!imageName.isNullOrEmpty()) {
                volumeCreate.bootable(true)
                volumeCreate.imageRef(imageService.getImage(imageName)!!.id)
            }

            client.blockStorage().volumes().create(volumeCreate.build())
        } else {
            createVolume(
                addSymbolsToCopy(volumeName),
                description,
                size,
                volumeType,
                imageName
            )
        }
    }

    fun updateVolume(volumeName: String, newName: String, description: String, newSize: Int) {
        val volumeId = getVolume(volumeName)!!.id

        client.blockStorage().volumes().update(volumeId, newName, description)
        if (newSize > 0) {
            client.blockStorage().volumes().extend(volumeId, newSize)
        }
    }

    fun deleteVolume(volumeName: String) {
        val volumeId = getVolume(volumeName)!!.id

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