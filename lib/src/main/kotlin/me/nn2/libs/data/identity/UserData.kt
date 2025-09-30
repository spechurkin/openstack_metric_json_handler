package me.nn2.libs.data.identity

data class UserData(
    val id: String?,
    val name: String?,
    val description: String?,
    val email: String? = null,
    val domain: String?,
    val enabled: Boolean?,
    val defaultProjectName: String? = null,
)