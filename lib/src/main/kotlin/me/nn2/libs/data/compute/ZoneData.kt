package me.nn2.libs.data.compute

import org.openstack4j.model.compute.ext.AvailabilityZone

data class ZoneData(
    val zoneName: String,
    val isAvailable: Boolean,
    val hosts: MutableMap<String, MutableMap<String, out AvailabilityZone.NovaService>>
)