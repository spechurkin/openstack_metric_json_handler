package org.opensearch.openstackMetricHandler

import org.opensearch.cluster.metadata.IndexNameExpressionResolver
import org.opensearch.cluster.node.DiscoveryNodes
import org.opensearch.common.settings.ClusterSettings
import org.opensearch.common.settings.IndexScopedSettings
import org.opensearch.common.settings.Settings
import org.opensearch.common.settings.SettingsFilter
import org.opensearch.openstackMetricHandler.config.OpenStackManager
import org.opensearch.openstackMetricHandler.action.BlockStorageMetricsHandlerAction
import org.opensearch.openstackMetricHandler.action.ComputeMetricsHandlerAction
import org.opensearch.openstackMetricHandler.action.ConfigHandlerAction
import org.opensearch.openstackMetricHandler.action.IdentityMetricsHandlerAction
import org.opensearch.openstackMetricHandler.action.NetworkingMetricsHandlerAction
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