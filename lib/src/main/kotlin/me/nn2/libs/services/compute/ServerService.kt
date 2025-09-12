package me.nn2.libs.services.compute

import me.nn2.libs.data.compute.ServerData
import me.nn2.libs.services.IMetricService
import org.openstack4j.api.OSClient.OSClientV3

class ServerService(override val client: OSClientV3) : IMetricService {
    fun getServers(): List<ServerData> {
        return convertToDto()
    }

    private fun convertToDto(): List<ServerData> {
        val serverData =
            client.compute().servers().list().map { server ->
                ServerData(
                    id = server.id,
                    name = server.name,
                    status = server.status,
                    flavor = FlavorService(client).convertToDto(server.flavor),
                    created = server.created,
                    updated = server.updated,
                )
            }
        return serverData
    }
}
