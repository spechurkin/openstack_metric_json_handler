package proj.work.consoleCreation.network

import org.openstack4j.api.Builders
import org.openstack4j.model.network.options.PortListOptions
import proj.work.consoleCreation.compute.getServerIdByName
import proj.work.consoleCreation.compute.os

fun main() {
    createFloating(
        getServerIdByName("DefaultServer"),
        "7fb16198-1e4a-4ae5-8abd-ca0dc372ac1c",
        getNetworkIdByName("MY_NETWORK")
    )
}

fun createFloating(serverId: String?, externalNetworkId: String, networkId: String?) {
    val port = os.networking().port().list(
        PortListOptions.create().deviceId(serverId).networkId(networkId)
    )[0]

    println(port.toString())

    val floatingIpCreate = Builders.netFloatingIP()
        .portId(port.id)
        .floatingNetworkId(externalNetworkId)
        .build()

    os.networking().floatingip().create(floatingIpCreate)
}

fun getFloatingIdByName(floatingIpName: String): String? {
    return os.networking().floatingip().list(mapOf("name" to floatingIpName)).firstOrNull()?.id
}