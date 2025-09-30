package me.nn2.libs.services.networking

import me.nn2.libs.data.networking.FloatingIpData
import me.nn2.libs.services.AbstractMetricService
import org.openstack4j.api.OSClient.OSClientV3
import org.openstack4j.model.network.NetFloatingIP

class FloatingIpsService(client: OSClientV3) : AbstractMetricService(client) {
    fun getFloatingIps(): List<FloatingIpData> {
        return convertToDto()
    }

    fun convertToDto(floatingIp: NetFloatingIP): FloatingIpData {
        return FloatingIpData(
            id = floatingIp.id,
            project = client.identity().projects().get(floatingIp.projectId).name,
            description = floatingIp.description ?: "",
            router = try {
                client.networking().router().get(floatingIp.routerId).name
            } catch (_: Exception) {
                "Маршрутизатор не найден"
            },
            status = floatingIp.status.toString(),
            fixedIpAddress = floatingIp.fixedIpAddress,
            floatingIpAddress = floatingIp.floatingIpAddress,
            floatingNetworkId = floatingIp.floatingNetworkId
        )
    }

    private fun convertToDto(): List<FloatingIpData> {
        return client.networking().floatingip().list().map {
            convertToDto(it)
        }
    }
}