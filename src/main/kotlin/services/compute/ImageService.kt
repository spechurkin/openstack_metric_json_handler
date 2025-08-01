package my.proj.work.services.compute

import my.proj.work.services.IMetricService
import org.openstack4j.api.OSClient.OSClientV3
import java.util.*

data class ImageDTO(
    var id: String?,
    var name: String?,
    var minRam: Int?,
    var minDisk: Int?,
    var size: Long?,
    var status: String?,
    var created: Date?,
    var updated: Date?
)

class ImageService(override val client: OSClientV3) : IMetricService {
    fun toJson(): String? {
        return toJson(convertToDto(), "compute/images")
    }

    private fun convertToDto(): List<ImageDTO> {
        val imageDto = client.compute().images().list().map { image ->
            ImageDTO(
                id = image.id,
                name = image.name,
                minRam = image.minRam,
                minDisk = image.minDisk,
                size = image.size,
                status = image.status.name,
                created = image.created,
                updated = image.updated
            )
        }
        return imageDto
    }
}