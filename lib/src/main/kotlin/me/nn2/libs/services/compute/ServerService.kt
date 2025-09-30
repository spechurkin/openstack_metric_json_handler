package me.nn2.libs.services.compute

import me.nn2.libs.data.compute.ServerData
import me.nn2.libs.services.AbstractMetricService
import me.nn2.libs.services.networking.NetworkService
import org.openstack4j.api.Builders
import org.openstack4j.api.OSClient.OSClientV3
import org.openstack4j.model.compute.Server
import org.openstack4j.model.compute.ServerUpdateOptions

class ServerService(client: OSClientV3) : AbstractMetricService(client) {
    fun getServers(): List<ServerData> {
        return convertToDto()
    }

    fun getServer(serverName: String): Server? {
        return client.compute().servers().list().firstOrNull { it.name == serverName }
    }

    fun createServer(
        serverName: String,
        imageName: String,
        flavorName: String,
        adminPass: String = "admin",
        keyPair: String? = null,
        networkNames: List<String>
    ) {
        if (getServer(serverName) == null) {
            val serverCreate = Builders.server()
                .name(serverName)
                .image(ImageService(client).getImage(imageName)!!.id)
                .flavor(FlavorService(client).getFlavor(flavorName)!!.id)
                .networks(networkNames.map { NetworkService(client).getNetwork(it)!!.id })
                .addAdminPass(adminPass)
                .apply {
                    keyPair?.let { keypairName(it) }
                }.build()

            client.compute().servers().boot(serverCreate)
        } else {
            createServer(addSymbolsToCopy(serverName), imageName, flavorName, adminPass, keyPair, networkNames)
        }
    }

    fun updateServer(
        serverName: String,
        ipv4Address: String?,
        ipv6Address: String?,
    ): ServerData {
        val serverBuilder = ServerUpdateOptions.create()
            .name(serverName)

        if (!ipv4Address.isNullOrEmpty()) {
            serverBuilder.accessIPv4(ipv4Address)
        }
        if (!ipv6Address.isNullOrEmpty()) {
            serverBuilder.accessIPv6(ipv6Address)
        }

        return convertToDto(client.compute().servers().update(getServer(serverName)!!.id, serverBuilder))
    }

    fun deleteServer(serverName: String) {
        client.compute().servers().delete(getServer(serverName)!!.id)
    }

    fun convertToDto(server: Server): ServerData {
        val flavor = server.flavor
        val flavorId = flavor?.id ?: "Не доступен"
        val flavorName = if (flavorId == "Не доступен") {
            flavorId
        } else flavor.name

        val image = server.image
        val imageId = image?.id ?: "-"
        val imageName = if (imageId == "-") {
            imageId
        } else image.name

        return ServerData(
            id = server.id,
            name = server.name,
            status = server.status.toString(),
            flavorName = flavorName,
            imageName = imageName,
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
