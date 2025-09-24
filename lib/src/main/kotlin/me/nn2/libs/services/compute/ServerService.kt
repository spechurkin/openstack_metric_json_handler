package me.nn2.libs.services.compute

import me.nn2.libs.data.compute.ServerData
import me.nn2.libs.services.IMetricService
import me.nn2.libs.services.networking.NetworkService
import org.openstack4j.api.Builders
import org.openstack4j.api.OSClient.OSClientV3
import org.openstack4j.model.compute.Server

class ServerService(override val client: OSClientV3) : IMetricService {
    private val imageService = ImageService(client)
    private val flavorService = FlavorService(client)
    private val networkService = NetworkService(client)

    fun getServers(): List<ServerData> {
        return convertToDto()
    }

    fun createServer(
        serverName: String,
        imageName: String,
        flavorName: String,
        adminPass: String = "admin",
        keyPair: String? = null,
        networkNames: List<String>
    ) {
        val serverCreate = Builders.server()
            .name(serverName)
            .image(imageService.getImageIdByName(imageName))
            .flavor(flavorService.getFlavorIdByName(flavorName))
            .networks(networkNames.map { networkService.getNetworkIdByName(it) })
            .addAdminPass(adminPass)
            .apply {
                keyPair?.let { keypairName(it) }
            }.build()

        client.compute().servers().boot(serverCreate)
    }

    fun getServerIdByName(serverName: String): String? {
        return client.compute().servers().list().find { it.name == serverName }?.id
    }

    fun convertToDto(server: Server): ServerData {
        return ServerData(
            id = server.id,
            name = server.name,
            status = server.status,
            flavor = FlavorService(client).convertToDto(server.flavor),
            addresses = server.addresses.addresses.map {
                it.key + ": " + it.value.map { address -> address.addr }
            },
            securityGroups = server.securityGroups?.map { it.name } ?: listOf(""),
            keyName = server.keyName,
            created = server.created,
            updated = server.updated
        )
    }

    private fun convertToDto(): List<ServerData> {
        return client.compute().servers().list().map { convertToDto(it) }
    }
}
