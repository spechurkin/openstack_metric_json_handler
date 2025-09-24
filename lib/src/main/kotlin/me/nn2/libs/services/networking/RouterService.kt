package me.nn2.libs.services.networking

import me.nn2.libs.data.networking.RouterData
import me.nn2.libs.services.IMetricService
import org.openstack4j.api.OSClient
import org.openstack4j.model.network.Router

class RouterService(override val client: OSClient.OSClientV3) : IMetricService {

    fun getRouters(): List<RouterData> {
        return convertToDto()
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