package proj.work.consoleCreation.network

import org.openstack4j.api.Builders
import proj.work.consoleCreation.compute.os

fun main() {
    createNetwork(os.identity().projects().list().find { it.name == "admin" }?.id, "DefaultNetwork")
    updateNetwork(
        getNetworkIdByName("DefaultNetwork"),
        "New Network",
        isShared = true,
        isAdmin = true
    )
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