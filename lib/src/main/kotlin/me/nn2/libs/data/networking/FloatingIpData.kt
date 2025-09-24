package me.nn2.libs.data.networking

data class FloatingIpData(
    val id: String,
    val tenantId: String,
    val router: String,
    val fixedIpAddress: String,
    val floatingNetworkId: String,
)
