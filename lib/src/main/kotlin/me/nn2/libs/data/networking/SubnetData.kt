package me.nn2.libs.data.networking

data class SubnetData(
    val id: String?,
    val name: String?,
    val ipVersion: String?,
    val dns: List<String>?,
    val pools: List<String?>?,
    val routes: List<String?>?,
    val gateway: String?,
    val _cidr: String?,
    val ipV6AddressMode: String?,
    val ipV6RaMode: String?,
)