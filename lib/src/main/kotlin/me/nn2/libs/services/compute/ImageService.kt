package me.nn2.libs.services.compute

import me.nn2.libs.data.compute.ImageData
import me.nn2.libs.services.IMetricService
import org.openstack4j.api.OSClient.OSClientV3
import org.openstack4j.model.image.v2.Image

class ImageService(override val client: OSClientV3) : IMetricService {
    fun getImages(): List<ImageData> {
        return convertToDto()
    }

    private fun convertToDto(): List<ImageData> {
        return client.imagesV2().list().map { image ->
            convertToDto(image)
        }
    }

    fun convertToDto(image: Image): ImageData {
        return ImageData(
            id = image.id,
            name = image.name,
            minRam = image.minRam,
            minDisk = image.minDisk,
            size = image.size,
            status = image.status,
            createdAt = image.createdAt,
            updatedAt = image.updatedAt,
        )
    }
}
