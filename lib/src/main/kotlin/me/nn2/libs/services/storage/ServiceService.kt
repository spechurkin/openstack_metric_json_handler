package me.nn2.libs.services.storage

import me.nn2.libs.data.ServiceData
import me.nn2.libs.services.AbstractMetricService
import org.openstack4j.api.OSClient.OSClientV3
import org.openstack4j.model.storage.block.ext.Service

class ServiceService(client: OSClientV3) : AbstractMetricService(client) {

    fun getServices(): List<ServiceData> {
        return convertToDto()
    }

    fun convertToDto(service: Service): ServiceData {
        return ServiceData(
            binary = service.binary,
            status = service.status.toString(),
            state = service.state.toString(),
            host = service.host,
            zone = service.zone,
            updatedAt = service.updatedAt
        )
    }

    private fun convertToDto(): List<ServiceData> {
        return client.blockStorage().services().list().map {
            convertToDto(it)
        }
    }
}