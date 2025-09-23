package me.nn2.libs.services.networking

import me.nn2.libs.data.networking.NetworkData
import me.nn2.libs.services.IMetricService
import org.openstack4j.api.OSClient
import org.openstack4j.model.network.Network

class NetworkService(override val client: OSClient.OSClientV3) : IMetricService {
    fun getNetworks(): List<NetworkData> {
        return convertToDto()
    }

    fun getNetworkIdByName(networkName: String): String? {
        return client.networking().network().list(mapOf("name" to networkName)).firstOrNull()?.id
    }

    fun convertToDto(network: Network): NetworkData {
        return NetworkData(
            id = network.id,
            name = network.name,
            status = network.status.toString(),
            external = network.isRouterExternal,
            shared = network.isShared,
            admin = network.isAdminStateUp,
            mtu = network.mtu,
            subnets = network.subnets,
        )
    }

    private fun convertToDto(): List<NetworkData> {
        return client.networking().network().list().map {
            convertToDto(it)
        }
    }
}