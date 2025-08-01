package my.proj.work.services.compute

import my.proj.work.services.IMetricService
import org.openstack4j.api.OSClient.OSClientV3
import java.util.*

data class ServerDTO(
    var id: String?,
    var name: String?,
    var status: String?,
    var flavor: FlavorDTO?,
    var created: Date?,
    var updated: Date?
)

class ServerService(override val client: OSClientV3) : IMetricService {
    fun toJson(): String? {
        return toJson(convertToDto(), "compute/servers")
    }

    private fun convertToDto(): List<ServerDTO> {
        val serverDto = client.compute().servers().list().map { server ->
            ServerDTO(
                id = server.id,
                name = server.name,
                status = server.status.name,
                flavor = FlavorService(client).convertToDto(server.flavor),
                created = server.created,
                updated = server.updated
            )
        }
        return serverDto
    }
}