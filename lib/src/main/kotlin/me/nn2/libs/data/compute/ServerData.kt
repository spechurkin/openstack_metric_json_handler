package me.nn2.libs.data.compute

import org.openstack4j.model.compute.Server.Status
import java.util.*

data class ServerData(
    var id: String?,
    var name: String?,
    var status: Status?,
    var flavor: FlavorData?,
    var created: Date?,
    var updated: Date?,
)