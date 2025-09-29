package me.nn2.libs.data.networking

data class PortData(
    val id: String,
    val name: String,
    val device: String,
    val network: String,
    val isAdmin: Boolean,
    val isSecured: Boolean,
    val secGroups: MutableList<String>,
)