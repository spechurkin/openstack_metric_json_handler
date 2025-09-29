package me.nn2.libs.services.networking

import me.nn2.libs.data.networking.PortData
import me.nn2.libs.services.IMetricService
import org.openstack4j.api.OSClient
import org.openstack4j.model.network.Port

class PortService(override val client: OSClient.OSClientV3) : IMetricService {

    fun getPorts(): List<PortData> {
        return convertToDto()
    }

    fun convertToDto(port: Port): PortData {
        return PortData(
            id = port.id,
            name = port.name,
            device = port.deviceId,
            network = client.networking().network().get(port.networkId).name,
            isAdmin = port.isAdminStateUp,
            isSecured = port.isPortSecurityEnabled,
            secGroups = port.securityGroups,
        )
    }

    private fun convertToDto(): List<PortData> {
        return client.networking().port().list().map {
            convertToDto(it)
        }
    }
}