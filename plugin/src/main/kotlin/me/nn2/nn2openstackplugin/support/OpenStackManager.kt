package me.nn2.nn2openstackplugin.support

import me.nn2.libs.OpenStackWrapper

class OpenStackManager() {
    fun wrapper(
        authUrl: String,
        username: String,
        password: String,
        domain: String = "Default",
        project: String = "admin",
        allowInsecure: Boolean = true
    ): OpenStackWrapper {
        return OpenStackWrapper(
            authUrl = authUrl,
            username = username,
            password = password,
            domain = domain,
            project = project,
            allowInsecure = allowInsecure
        )
    }
}