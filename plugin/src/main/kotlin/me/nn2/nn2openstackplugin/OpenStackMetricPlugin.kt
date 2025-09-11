package me.nn2.nn2openstackplugin

import me.nn2.nn2openstackplugin.action.BlockStorageMetricsHandlerAction
import me.nn2.nn2openstackplugin.action.ComputeMetricsHandlerAction
import me.nn2.nn2openstackplugin.action.ConfigHandlerAction
import me.nn2.nn2openstackplugin.action.IdentityMetricsHandlerAction
import me.nn2.nn2openstackplugin.action.NetworkingMetricsHandlerAction
import org.opensearch.cluster.metadata.IndexNameExpressionResolver
import org.opensearch.cluster.node.DiscoveryNodes
import org.opensearch.common.settings.ClusterSettings
import org.opensearch.common.settings.IndexScopedSettings
import org.opensearch.common.settings.Settings
import org.opensearch.common.settings.SettingsFilter
import me.nn2.nn2openstackplugin.config.OpenStackManager
import org.opensearch.plugins.ActionPlugin
import org.opensearch.plugins.Plugin
import org.opensearch.rest.RestController
import org.opensearch.rest.RestHandler
import java.util.function.Supplier

class OpenStackMetricPlugin : Plugin(), ActionPlugin {
    private val manager = OpenStackManager()

    override fun getRestHandlers(
        settings: Settings?,
        restController: RestController?,
        clusterSettings: ClusterSettings?,
        indexScopedSettings: IndexScopedSettings?,
        settingsFilter: SettingsFilter?,
        indexNameExpressionResolver: IndexNameExpressionResolver?,
        nodesInCluster: Supplier<DiscoveryNodes?>?
    ): List<RestHandler?>? {
        return listOf(
            ComputeMetricsHandlerAction(manager),
            BlockStorageMetricsHandlerAction(manager),
            NetworkingMetricsHandlerAction(manager),
            IdentityMetricsHandlerAction(manager),
            ConfigHandlerAction(manager)
        )
    }
}