package me.nn2.libs.services.storage

import me.nn2.libs.data.storage.VolumeData
import me.nn2.libs.services.IMetricService
import me.nn2.libs.services.compute.ImageService
import org.openstack4j.api.Builders
import org.openstack4j.api.OSClient

class VolumeService(override val client: OSClient.OSClientV3) : IMetricService {
    val imageService = ImageService(client)

    fun getVolume(): List<VolumeData> {
        return convertToDto()
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

    fun getVolumeIdByName(volumeName: String): String? {
        return client.blockStorage().volumes().list().find { it.name == volumeName }?.id
    }

    private fun convertToDto(): List<VolumeData> {
        return client.blockStorage().volumes().list().map { volume ->
            VolumeData(
                id = volume.id,
                name = volume.name,
                description = volume.description,
                size = volume.size,
                status = volume.status,
                metadata = volume.metaData.map { (key, value) ->
                    StringBuilder().append(key).append("=").append(value).toString()
                },
                created = volume.created
            )
        }
    }
}