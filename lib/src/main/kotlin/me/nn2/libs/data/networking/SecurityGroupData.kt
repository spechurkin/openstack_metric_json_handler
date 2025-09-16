package me.nn2.libs.data.networking

data class SecurityGroupData(
    val id: String?,
    val name: String?,
    val description: String?,
    val rules: List<String>?,
)
