package org.opensearch.openstackMetric

import org.opensearch.cluster.metadata.IndexNameExpressionResolver
import org.opensearch.cluster.node.DiscoveryNodes
import org.opensearch.common.settings.ClusterSettings
import org.opensearch.common.settings.IndexScopedSettings
import org.opensearch.common.settings.Settings
import org.opensearch.common.settings.SettingsFilter
import org.opensearch.openstackMetric.config.OpenStackManager
import org.opensearch.openstackMetric.action.BlockStorageMetricsHandlerAction
import org.opensearch.openstackMetric.action.ComputeMetricsHandlerAction
import org.opensearch.openstackMetric.action.ConfigHandlerAction
import org.opensearch.openstackMetric.action.IdentityMetricsHandlerAction
import org.opensearch.openstackMetric.action.NetworkingMetricsHandlerAction
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