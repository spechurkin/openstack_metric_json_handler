package proj.work.consoleCreation.network

import org.openstack4j.api.Builders
import org.openstack4j.model.network.AttachInterfaceType
import proj.work.consoleCreation.compute.os

fun main() {
    createRouter("DefaultRouter", true, getNetworkIdByName("DefaultNetwork"), true)
    updateRouter("DefaultRouter", "New Router", true)
}

fun createRouter(
    routerName: String,
    adminState: Boolean = true,
    networkId: String?,
    isSnat: Boolean = false,
    routes: Map<String, String> = mapOf()
) {
    val routerCreate = Builders.router()
        .name(routerName)
        .adminStateUp(adminState)
        .externalGateway(networkId, isSnat)
    if (routes.isNotEmpty()) {
        routerCreate.route(routes.entries.first().key, routes.entries.first().value)
    } else {
        routerCreate.noRoutes()
    }

    os.networking().router().create(routerCreate.build())
}

fun updateRouter(routerName: String, newName: String, adminState: Boolean) {
    val routerForUpdate = os.networking().router().list().find { it.name == routerName }
    val routerBuilder = routerForUpdate!!.toBuilder()
        .name(newName)
    if (routerForUpdate.isAdminStateUp == adminState) {
        routerBuilder.adminStateUp(adminState)
    } else {
        routerBuilder.adminStateUp(adminState)
    }

    os.networking().router().update(routerBuilder.build())
}

fun attachInterface(routerId: String, subnetId: String) {
    os.networking().router().attachInterface(routerId, AttachInterfaceType.SUBNET, subnetId)
}

fun detachInterface(routerId: String, subnetId: String) {
    os.networking().router().detachInterface(routerId, subnetId, null)
}

fun deleteRouter(routerId: String) {
    os.networking().router().delete(routerId)
}

fun getRouterIdByName(routerName: String): String? {
    return os.networking().router().list().find { it.name == routerName }!!.id
}