package me.nn2.libs.services.compute

import me.nn2.libs.data.compute.ZoneData
import me.nn2.libs.services.IMetricService
import org.openstack4j.api.OSClient
import org.openstack4j.model.compute.ext.AvailabilityZone

class ZoneService(override val client: OSClient.OSClientV3) : IMetricService {

    fun getZones(): List<ZoneData> {
        return convertToDto()
    }

    fun convertToDto(zone: AvailabilityZone): ZoneData {
        return ZoneData(
            zoneName = zone.zoneName,
            isAvailable = zone.zoneState.available,
            hosts = zone.hosts
        )
    }

    private fun convertToDto(): List<ZoneData> {
        return client.compute().zones().list().map {
            convertToDto(it)
        }
    }
}