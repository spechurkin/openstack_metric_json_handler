package me.nn2.libs.services.networking

import me.nn2.libs.data.networking.RouterData
import me.nn2.libs.services.AbstractMetricService
import me.nn2.libs.services.IdentityService
import org.openstack4j.api.Builders.router
import org.openstack4j.api.OSClient.OSClientV3
import org.openstack4j.model.network.Router

class RouterService(client: OSClientV3) : AbstractMetricService(client) {
    private val projectService = IdentityService(client)
    private val networkService = NetworkService(client)

    fun getRouters(): List<RouterData> {
        return convertToDto()
    }

    fun getRouter(routerName: String): Router? {
        return client.networking().router().list().firstOrNull { it.name == routerName }
    }

    fun createRouter(
        routerName: String,
        projectName: String,
        isDistributed: Boolean,
        idAdmin: Boolean,
        destination: String?,
        nextHop: String?,
        networkName: String,
        enableSnat: Boolean
    ) {
        if (getRouter(routerName) == null) {
            val routerBuilder = router()
                .name(routerName)
                .tenantId(projectService.getProject(projectName)!!.id)
                .distributed(isDistributed)
                .adminStateUp(idAdmin)
                .externalGateway(networkService.getNetwork(networkName)!!.id, enableSnat)

            if (destination != null && nextHop != null) {
                routerBuilder.route(destination, nextHop)
            } else routerBuilder.noRoutes()

            client.networking().router().create(routerBuilder.build())
        } else {
            createRouter(
                addSymbolsToCopy(routerName),
                projectName,
                isDistributed,
                idAdmin,
                destination,
                nextHop,
                networkName,
                enableSnat
            )
        }
    }

    fun updateRouter(
        routerName: String,
        newName: String,
        idAdmin: Boolean,
        networkName: String?
    ) {
        val routerBuilder = router()
            .id(getRouter(routerName)!!.id)
            .name(newName)
            .adminStateUp(idAdmin)
        if (networkName?.isNotEmpty() == true) {
            routerBuilder.externalGateway(networkService.getNetwork(networkName)!!.id)
        }

        client.networking().router().update(routerBuilder.build())
    }

    fun deleteRouter(routerName: String) {
        client.networking().router().delete(getRouter(routerName)!!.id)
    }

    fun convertToDto(router: Router): RouterData {
        return RouterData(
            id = router.id,
            name = router.name,
            status = router.status.toString(),
            distributed = router.distributed,
            isAdmin = router.isAdminStateUp,
            routes = router.routes.map { it.toString() },
            externalNetworkId = router.externalGatewayInfo.networkId,
            snat = router.externalGatewayInfo.isEnableSnat
        )
    }

    private fun convertToDto(): List<RouterData> {
        return client.networking().router().list().map {
            convertToDto(it)
        }
    }
}