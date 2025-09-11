package services.compute

import org.openstack4j.api.OSClient.OSClientV3
import org.openstack4j.model.compute.Image.Status
import services.IMetricService
import java.util.*

class ImageService(override val client: OSClientV3) : IMetricService {
    fun getImages(): List<ImageDTO> {
        return convertToDto()
    }

    private fun convertToDto(): List<ImageDTO> {
        val imageDto =
            client.compute().images().list().map { image ->
                ImageDTO(
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
        return imageDto
    }

    data class ImageDTO(
        var id: String?,
        var name: String?,
        var minRam: Int?,
        var minDisk: Int?,
        var size: Long?,
        var status: Status?,
        var created: Date?,
        var updated: Date?,
    )
}
