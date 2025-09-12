package me.nn2.nn2openstackplugin.support.settings

data class OpenStackConfig(
    val authUrl: String,
    val username: String,
    val password: String,
    val domain: String = "Default",
    val project: String = "admin",
    val allowInsecure: Boolean = true
)