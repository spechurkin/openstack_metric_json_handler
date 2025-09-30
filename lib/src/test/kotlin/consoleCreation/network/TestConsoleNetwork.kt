package proj.work.consoleCreation.network

import me.nn2.libs.data.networking.NetworkData
import me.nn2.libs.services.networking.SubnetService
import org.openstack4j.api.Builders
import org.openstack4j.model.network.Network
import proj.work.consoleCreation.compute.os

fun main() {
    println(getNetwork("MY_NETWORK3"))
}

fun getNetworks(): List<NetworkData> {
    return convertToDto()
}

fun getNetwork(networkName: String): NetworkData? {
    return getNetworks().firstOrNull { it.name == networkName }
}

fun createNetwork(projectId: String?, networkName: String) {
    val networkCreate = Builders.network()
        .name(networkName)
        .tenantId(projectId)
        .build()

    os.networking().network().create(networkCreate)
}

fun updateNetwork(networkId: String?, newName: String, isShared: Boolean, isAdmin: Boolean) {
    os.networking().network().update(
        networkId,
        Builders.networkUpdate()
            .name(newName)
            .shared(isShared)
            .adminStateUp(isAdmin)
            .build()
    )
}

fun deleteNetwork(networkId: String?) {
    os.networking().network().delete(networkId)
}

fun getNetworkIdByName(networkName: String): String? {
    return os.networking().network().list().find { it.name == networkName }?.id
}

fun convertToDto(network: Network): NetworkData {
    val subnets = SubnetService(os).getNetworkSubnets(network.name).map { "${it.name} (${it._cidr})" }

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
    return os.networking().network().list().map {
        convertToDto(it)
    }
}