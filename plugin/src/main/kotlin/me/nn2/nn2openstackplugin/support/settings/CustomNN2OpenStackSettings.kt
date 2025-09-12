package me.nn2.nn2openstackplugin.support.settings

import org.opensearch.common.settings.Setting

object CustomNN2OpenStackSettings {
    val SETTING_OPENSTACK_AUTH_URL: Setting<String> =
        Setting.simpleString(
            ConfigHelper.SETTING_OPENSTACK_AUTH_URL,
            "",
            Setting.Property.NodeScope,
            Setting.Property.Dynamic
        )
    val SETTING_OPENSTACK_USER: Setting<String> =
        Setting.simpleString(
            ConfigHelper.SETTING_OPENSTACK_USER,
            "",
            Setting.Property.NodeScope,
            Setting.Property.Dynamic
        )
    val SETTING_OPENSTACK_PASS: Setting<String> =
        Setting.simpleString(
            ConfigHelper.SETTING_OPENSTACK_PASS,
            "",
            Setting.Property.NodeScope,
            Setting.Property.Dynamic
        )
    val SETTING_OPENSTACK_DOMAIN: Setting<String> =
        Setting.simpleString(
            ConfigHelper.SETTING_OPENSTACK_DOMAIN,
            "",
            Setting.Property.NodeScope,
            Setting.Property.Dynamic
        )
    val SETTING_OPENSTACK_PROJECT: Setting<String> =
        Setting.simpleString(
            ConfigHelper.SETTING_OPENSTACK_PROJECT,
            "",
            Setting.Property.NodeScope,
            Setting.Property.Dynamic
        )
    val SETTING_OPENSTACK_INSECURE: Setting<Boolean> =
        Setting.boolSetting(
            ConfigHelper.SETTING_OPENSTACK_INSECURE,
            true,
            Setting.Property.NodeScope,
            Setting.Property.Dynamic
        )
}