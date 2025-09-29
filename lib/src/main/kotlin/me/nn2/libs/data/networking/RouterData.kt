package me.nn2.libs.data.networking

data class RouterData(
    val id: String,
    val name: String?,
    val status: String?,
    val distributed: Boolean?,
    val isAdmin: Boolean?,
    val routes: List<String>?,
    val externalNetworkId: String?,
    val snat: Boolean?,
)