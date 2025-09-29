package me.nn2.libs.services.networking

import me.nn2.libs.data.networking.FloatingIpData
import me.nn2.libs.services.IMetricService
import org.openstack4j.api.OSClient
import org.openstack4j.model.network.NetFloatingIP

class FloatingIpsService(override val client: OSClient.OSClientV3) : IMetricService {
    private val routerService = RouterService(client)

    fun getFloatingIps(): List<FloatingIpData> {
        return convertToDto()
    }

    fun convertToDto(floatingIp: NetFloatingIP): FloatingIpData {
        return FloatingIpData(
            id = floatingIp.id,
            tenantId = floatingIp.tenantId,
            router = try {
                client.networking().router().get(floatingIp.routerId).name
            } catch (_: Exception) {
                ""
            },
            fixedIpAddress = floatingIp.fixedIpAddress,
            floatingNetworkId = floatingIp.floatingNetworkId
        )
    }

    private fun convertToDto(): List<FloatingIpData> {
        return client.networking().floatingip().list().map {
            convertToDto(it)
        }
    }
}