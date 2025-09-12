package me.nn2.nn2openstackplugin.support.settings

object ConfigHelper {
    private const val BASE_PREFIX = "openstack."

    val SETTING_OPENSTACK_AUTH_URL: String = String.format("%s%s", BASE_PREFIX, "authUrl")
    val SETTING_OPENSTACK_USER: String = String.format("%s%s", BASE_PREFIX, "user")
    val SETTING_OPENSTACK_PASS: String = String.format("%s%s", BASE_PREFIX, "pass")
    val SETTING_OPENSTACK_DOMAIN: String = String.format("%s%s", BASE_PREFIX, "domain")
    val SETTING_OPENSTACK_PROJECT: String = String.format("%s%s", BASE_PREFIX, "project")
    val SETTING_OPENSTACK_INSECURE: String = String.format("%s%s", BASE_PREFIX, "insecure")
}