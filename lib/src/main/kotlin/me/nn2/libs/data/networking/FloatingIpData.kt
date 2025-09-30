package me.nn2.libs.data.networking

data class FloatingIpData(
    val id: String,
    val project: String,
    val description: String?,
    val status: String,
    val router: String,
    val fixedIpAddress: String?,
    val floatingIpAddress: String,
    val floatingNetworkId: String?,
)
