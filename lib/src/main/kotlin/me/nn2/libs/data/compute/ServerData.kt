package me.nn2.libs.data.compute

import java.util.*

data class ServerData(
    var id: String?,
    var name: String?,
    var status: String,
    var flavorName: String?,
    var imageName: String?,
    var addresses: List<String>?,
    var securityGroups: List<String?>,
    var created: Date?,
    var updated: Date?,
    val keyName: String?,
)