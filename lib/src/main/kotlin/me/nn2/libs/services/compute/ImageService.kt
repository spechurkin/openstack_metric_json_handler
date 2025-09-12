package me.nn2.libs.services.compute

import me.nn2.libs.data.compute.ImageData
import me.nn2.libs.services.IMetricService
import org.openstack4j.api.OSClient.OSClientV3

class ImageService(override val client: OSClientV3) : IMetricService {
    fun getImages(): List<ImageData> {
        return convertToDto()
    }

    private fun convertToDto(): List<ImageData> {
        val imageData =
            client.compute().images().list().map { image ->
                ImageData(
                    id = image.id,
                    name = image.name,
                    minRam = image.minRam,
                    minDisk = image.minDisk,
                    size = image.size,
                    status = image.status,
                    created = image.created,
                    updated = image.updated,
                )
            }
        return imageData
    }
}
