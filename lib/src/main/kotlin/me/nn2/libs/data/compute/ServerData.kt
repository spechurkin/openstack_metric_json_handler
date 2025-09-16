package me.nn2.libs.data.compute

import org.openstack4j.model.compute.Server
import java.util.*

data class ServerData(
    var id: String?,
    var name: String?,
    var status: Server.Status?,
    var flavor: FlavorData?,
    var addresses: List<String?>,
    var securityGroups: List<String?>,
    var created: Date?,
    var updated: Date?,
    val keyName: String?,
)