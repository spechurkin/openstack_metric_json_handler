package me.nn2.libs.services.networking

import me.nn2.libs.data.networking.NetworkData
import me.nn2.libs.services.IMetricService
import me.nn2.libs.services.IdentityService
import org.openstack4j.api.Builders
import org.openstack4j.api.OSClient
import org.openstack4j.model.network.Network
import org.openstack4j.model.network.NetworkType

class NetworkService(override val client: OSClient.OSClientV3) : IMetricService {

    private val identityService = IdentityService(client)

    fun getNetworks(): List<NetworkData> {
        return convertToDto()
    }

    fun getNetworkIdByName(networkName: String): String? {
        return client.networking().network().list(mapOf("name" to networkName)).firstOrNull()?.id
    }

    fun createNetwork(
        projectName: String,
        networkName: String,
        isShared: Boolean,
        isExternal: Boolean,
        isAdmin: Boolean,
        networkType: NetworkType,
        physicalNetwork: String?,
        segmentId: String?
    ) {
        val networkCreate = Builders.network()
            .name(networkName)
            .tenantId(identityService.getProjectIdByName(projectName))
            .adminStateUp(isAdmin)
            .isRouterExternal(isExternal)
            .isShared(isShared)
            .networkType(networkType)
            .physicalNetwork(physicalNetwork)
            .segmentId(segmentId)
            .build()

        client.networking().network().create(networkCreate)
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