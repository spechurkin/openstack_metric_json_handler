package me.nn2.libs.services.compute

import me.nn2.libs.data.compute.HostAggregateData
import me.nn2.libs.services.IMetricService
import org.openstack4j.api.OSClient
import org.openstack4j.model.compute.HostAggregate

class HostAggregateService(override val client: OSClient.OSClientV3) : IMetricService {

    fun getHostAggregate(): List<HostAggregateData> {
        return convertToDto()
    }

    fun convertToDto(hostAggregate: HostAggregate): HostAggregateData {
        return HostAggregateData(
            hostAggregate.id,
            hostAggregate.name,
            hostAggregate.hosts,
            hostAggregate.availabilityZone,
            hostAggregate.create,
            hostAggregate.updatedAt,
            hostAggregate.isDeleted,
        )
    }

    private fun convertToDto(): List<HostAggregateData> {
        return client.compute().hostAggregates().list().map {
            convertToDto(it)
        }
    }
}