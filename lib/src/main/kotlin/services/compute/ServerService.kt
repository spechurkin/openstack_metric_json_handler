package services.compute

import org.openstack4j.api.OSClient.OSClientV3
import org.openstack4j.model.compute.Server.Status
import services.IMetricService
import java.util.*

class ServerService(override val client: OSClientV3) : IMetricService {
    fun getServers(): List<ServerDTO> {
        return convertToDto()
    }

    private fun convertToDto(): List<ServerDTO> {
        val serverDto =
            client.compute().servers().list().map { server ->
                ServerDTO(
                    id = server.id,
                    name = server.name,
                    status = server.status,
                    flavor = FlavorService(client).convertToDto(server.flavor),
                    created = server.created,
                    updated = server.updated,
                )
            }
        return serverDto
    }

    data class ServerDTO(
        var id: String?,
        var name: String?,
        var status: Status?,
        var flavor: FlavorService.FlavorDTO?,
        var created: Date?,
        var updated: Date?,
    )
}
