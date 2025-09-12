package me.nn2.nn2openstackplugin.support.settings

import org.opensearch.OpenSearchException
import org.opensearch.common.settings.Settings
import org.opensearch.env.Environment
import java.io.IOException

class SettingsLoader {
    fun loadSettingsFromFile(environment: Environment): Settings {
        val configDir = environment.configFile()
        val customSettingsYamlFile = configDir.resolve("nn2/openstack_plugin.yml")

        // Load the settings from the plugin's custom settings file
        val customSettings: Settings
        try {
            customSettings = Settings.builder().loadFromPath(customSettingsYamlFile).build()
            return customSettings
        } catch (e: IOException) {
            throw OpenSearchException("Failed to load settings", e)
        }
    }

    fun initSettingFields(
        settings: Settings?,
        globalSettings: GlobalSettings,
    ) {
        globalSettings.authUrl = CustomNN2OpenStackSettings.SETTING_OPENSTACK_AUTH_URL.get(settings)
        globalSettings.openstackUser = CustomNN2OpenStackSettings.SETTING_OPENSTACK_USER.get(settings)
        globalSettings.openstackPassword = CustomNN2OpenStackSettings.SETTING_OPENSTACK_PASS.get(settings)
        globalSettings.domain = CustomNN2OpenStackSettings.SETTING_OPENSTACK_DOMAIN.get(settings)
        globalSettings.project = CustomNN2OpenStackSettings.SETTING_OPENSTACK_PROJECT.get(settings)
        globalSettings.allowInsecure = CustomNN2OpenStackSettings.SETTING_OPENSTACK_INSECURE.get(settings)
    }
}