package me.nn2.nn2openstackplugin

import me.nn2.nn2openstackplugin.handlers.BlockStorageMetricsAction
import me.nn2.nn2openstackplugin.handlers.ComputeMetricsAction
import me.nn2.nn2openstackplugin.handlers.ConfigAction
import me.nn2.nn2openstackplugin.handlers.IdentityMetricsAction
import me.nn2.nn2openstackplugin.handlers.NetworkingMetricsAction
import org.opensearch.cluster.metadata.IndexNameExpressionResolver
import org.opensearch.cluster.node.DiscoveryNodes
import org.opensearch.common.settings.ClusterSettings
import org.opensearch.common.settings.IndexScopedSettings
import org.opensearch.common.settings.Settings
import org.opensearch.common.settings.SettingsFilter
import me.nn2.nn2openstackplugin.support.OpenStackManager
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
            ComputeMetricsAction(manager),
            BlockStorageMetricsAction(manager),
            NetworkingMetricsAction(manager),
            IdentityMetricsAction(manager),
            ConfigAction(manager)
        )
    }
}