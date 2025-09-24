package me.nn2.libs.data.networking

data class NetworkData(
    val id: String?,
    val name: String?,
    val status: String,
    val external: Boolean,
    val shared: Boolean,
    val tenantId: String,
    val admin: Boolean,
    val mtu: Int,
    val subnets: MutableList<String>,
)