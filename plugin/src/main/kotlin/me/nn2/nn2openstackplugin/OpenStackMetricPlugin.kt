package me.nn2.nn2openstackplugin

import me.nn2.nn2openstackplugin.handlers.BlockStorageMetricsAction
import me.nn2.nn2openstackplugin.handlers.ComputeMetricsAction
import me.nn2.nn2openstackplugin.handlers.IdentityMetricsAction
import me.nn2.nn2openstackplugin.handlers.NetworkingMetricsAction
import me.nn2.nn2openstackplugin.support.settings.CustomNN2OpenStackSettings
import me.nn2.nn2openstackplugin.support.settings.GlobalSettings
import me.nn2.nn2openstackplugin.support.settings.SettingsLoader
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.opensearch.cluster.metadata.IndexNameExpressionResolver
import org.opensearch.cluster.node.DiscoveryNodes
import org.opensearch.common.settings.*
import org.opensearch.env.Environment
import org.opensearch.plugins.ActionPlugin
import org.opensearch.plugins.Plugin
import org.opensearch.rest.RestController
import org.opensearch.rest.RestHandler
import java.nio.file.Path
import java.util.function.Supplier

class OpenStackMetricPlugin(private val settings: Settings, private val configPath: Path) : Plugin(), ActionPlugin {
    private val logger: Logger = LogManager.getLogger(OpenStackMetricPlugin::class.java)
    private var globalSettings = GlobalSettings()

    override fun getRestHandlers(
        settings: Settings?,
        restController: RestController?,
        clusterSettings: ClusterSettings?,
        indexScopedSettings: IndexScopedSettings?,
        settingsFilter: SettingsFilter?,
        indexNameExpressionResolver: IndexNameExpressionResolver?,
        nodesInCluster: Supplier<DiscoveryNodes?>?
    ): List<RestHandler?>? {
        val loader = SettingsLoader()
        loader.initSettingFields(loader.loadSettingsFromFile(Environment(settings, configPath)), globalSettings)

        return listOf(
            ComputeMetricsAction(globalSettings),
            BlockStorageMetricsAction(globalSettings),
            NetworkingMetricsAction(globalSettings),
            IdentityMetricsAction(globalSettings)
        )
    }

    override fun getSettings(): List<Setting<*>> {
        logger.info("Registering new settings...")

        return listOf<Setting<*>>(
            CustomNN2OpenStackSettings.SETTING_OPENSTACK_AUTH_URL,
            CustomNN2OpenStackSettings.SETTING_OPENSTACK_USER,
            CustomNN2OpenStackSettings.SETTING_OPENSTACK_PASS,
            CustomNN2OpenStackSettings.SETTING_OPENSTACK_DOMAIN,
            CustomNN2OpenStackSettings.SETTING_OPENSTACK_PROJECT,
            CustomNN2OpenStackSettings.SETTING_OPENSTACK_INSECURE
        )
    }
}