package me.nn2.libs.services.storage

import me.nn2.libs.data.ServiceData
import me.nn2.libs.services.IMetricService
import org.openstack4j.api.OSClient
import org.openstack4j.model.storage.block.ext.Service

class ServiceService(override val client: OSClient.OSClientV3) : IMetricService {

    fun getServices(): List<ServiceData> {
        return convertToDto()
    }

    fun convertToDto(service: Service): ServiceData {
        return ServiceData(
            id = service.id,
            status = service.status.toString(),
            state = service.state.toString(),
            host = service.host,
            zone = service.zone,
            reasonDisabled = service.disabledReason,
            updatedAt = service.updatedAt
        )
    }

    private fun convertToDto(): List<ServiceData> {
        return client.blockStorage().services().list().map {
            convertToDto(it)
        }
    }
}