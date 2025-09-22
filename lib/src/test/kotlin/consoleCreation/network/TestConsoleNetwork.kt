package proj.work.consoleCreation.network

import org.openstack4j.api.Builders
import proj.work.consoleCreation.compute.os

fun main(args: Array<String>) {
    createNetwork(args)
    updateNetwork(args.getOrElse(0) { "DefaultNetwork" }, "New Network", isShared = true, isAdmin = true)
}

fun createNetwork(args: Array<String>) {
    val projectId =
        args.getOrElse(1) { os.identity().projects().list().find { project -> project.name == "admin" }?.id }

    val networkCreate = Builders.network()
        .name(args.getOrElse(0) { "DefaultNetwork" })
        .tenantId(projectId)
        .build()

    os.networking().network().create(networkCreate)
}

fun updateNetwork(networkName: String, newName: String, isShared: Boolean, isAdmin: Boolean) {
    os.networking().network().update(
        getNetworkIdByName(networkName),
        Builders.networkUpdate()
            .name(newName)
            .shared(isShared)
            .adminStateUp(isAdmin)
            .build()
    )
}

fun getNetworkIdByName(networkName: String): String? {
    return os.networking().network().list(mapOf("name" to networkName)).firstOrNull()?.id
}