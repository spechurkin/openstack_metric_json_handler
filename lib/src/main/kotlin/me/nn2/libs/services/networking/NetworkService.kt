package me.nn2.libs.services.networking

import me.nn2.libs.data.networking.NetworkData
import me.nn2.libs.services.AbstractMetricService
import me.nn2.libs.services.IdentityService
import org.openstack4j.api.Builders
import org.openstack4j.api.OSClient.OSClientV3
import org.openstack4j.model.network.Network
import org.openstack4j.model.network.NetworkType

class NetworkService(client: OSClientV3) : AbstractMetricService(client) {
    private val identityService = IdentityService(client)

    fun getNetworks(): List<NetworkData> {
        return convertToDto()
    }

    fun getNetwork(networkName: String): Network? {
        return client.networking().network().list().firstOrNull { it.name == networkName }
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
        if (getNetwork(networkName) == null) {
            val networkCreate = Builders.network()
                .name(networkName)
                .tenantId(identityService.getProject(projectName)!!.id)
                .adminStateUp(isAdmin)
                .isRouterExternal(isExternal)
                .isShared(isShared)
                .networkType(networkType)
                .physicalNetwork(physicalNetwork)
                .segmentId(segmentId)
                .build()

            client.networking().network().create(networkCreate)
        } else {
            createNetwork(
                projectName,
                addSymbolsToCopy(networkName),
                isShared,
                isExternal,
                isAdmin,
                networkType,
                physicalNetwork,
                segmentId
            )
        }
    }

    fun updateNetwork(
        networkName: String,
        newName: String,
        isAdmin: Boolean,
        isShared: Boolean,
        isDefault: Boolean
    ) {
        val networkUpdate = Builders.networkUpdate()
            .name(newName)
            .adminStateUp(isAdmin)
            .shared(isShared)
            .isDefault(isDefault)
            .build()

        client.networking().network().update(getNetwork(networkName)!!.id, networkUpdate)
    }

    fun deleteNetwork(networkName: String) {
        client.networking().network().delete(getNetwork(networkName)!!.id)
    }

    fun convertToDto(network: Network): NetworkData {
        val subnets = SubnetService(client).getNetworkSubnets(network.name).map { "${it.name} (${it._cidr})" }

        return NetworkData(
            id = network.id,
            name = network.name,
            status = network.status.toString(),
            external = network.isRouterExternal,
            shared = network.isShared,
            tenantId = network.tenantId,
            admin = network.isAdminStateUp,
            mtu = network.mtu,
            subnets = subnets
        )
    }

    private fun convertToDto(): List<NetworkData> {
        return client.networking().network().list().map {
            convertToDto(it)
        }
    }
}