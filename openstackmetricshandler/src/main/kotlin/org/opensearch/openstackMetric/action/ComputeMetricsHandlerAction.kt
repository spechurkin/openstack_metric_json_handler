package org.opensearch.openstackMetric.action

import org.opensearch.common.Table
import org.opensearch.core.rest.RestStatus
import org.opensearch.openstackMetric.config.OpenStackManager
import org.opensearch.openstackMetric.config.err
import org.opensearch.openstackMetric.config.gson
import org.opensearch.openstackMetric.config.okJson
import org.opensearch.rest.RestChannel
import org.opensearch.rest.RestHandler
import org.opensearch.rest.RestRequest
import org.opensearch.rest.action.cat.AbstractCatAction
import org.opensearch.transport.client.node.NodeClient

class ComputeMetricsHandlerAction(private val manager: OpenStackManager) : AbstractCatAction() {
    override fun routes(): List<RestHandler.Route?>? {
        return listOf(
            RestHandler.Route(RestRequest.Method.GET, "/nn2/compute/servers"),
            RestHandler.Route(RestRequest.Method.GET, "/nn2/compute/images"),
            RestHandler.Route(RestRequest.Method.GET, "/nn2/compute/flavors"),
            RestHandler.Route(RestRequest.Method.GET, "/nn2/compute/keypairs"),
            RestHandler.Route(RestRequest.Method.GET, "/nn2/compute/services"),
            RestHandler.Route(RestRequest.Method.GET, "/nn2/compute/floatingIps"),
            RestHandler.Route(RestRequest.Method.GET, "/nn2/compute/hosts"),
            RestHandler.Route(RestRequest.Method.GET, "/nn2/compute/zones"),
            RestHandler.Route(RestRequest.Method.GET, "/nn2/compute/migrations"),
            RestHandler.Route(RestRequest.Method.GET, "/nn2/compute/hypervisors"),
            RestHandler.Route(RestRequest.Method.GET, "/nn2/compute/hostAggregates"),
            RestHandler.Route(RestRequest.Method.GET, "/nn2/compute/serverGroups"),
            RestHandler.Route(RestRequest.Method.GET, "/nn2/compute/securityGroups"),
            RestHandler.Route(RestRequest.Method.GET, "/nn2/compute/securityRules")
        )
    }

    override fun doCatRequest(
        rr: RestRequest?,
        nc: NodeClient?
    ): RestChannelConsumer? {
        val metric = rr?.path()?.split("/".toRegex())?.last()

        return RestChannelConsumer { channel: RestChannel ->
            try {
                val wrapper = manager.wrapper().compute()
                var dto: Any = ""
                when (metric) {
                    "servers" -> dto = wrapper.getServers()
                    "images" -> dto = wrapper.getImages()
                    "flavors" -> dto = wrapper.getFlavors()
                    "keypairs" -> dto = wrapper.getKeypairs()!!
                    "services" -> dto = wrapper.getServices()!!
                    "floatingIps" -> dto = wrapper.getFloatingIps()!!
                    "hosts" -> dto = wrapper.getHosts()!!
                    "zones" -> dto = wrapper.getZones()!!
                    "migrations" -> dto = wrapper.getMigrations()!!
                    "hypervisors" -> dto = wrapper.getHypervisors()!!
                    "hostAggregates" -> dto = wrapper.getHostAggregates()!!
                    "serverGroups" -> dto = wrapper.getServerGroups()!!
                    "securityGroups" -> dto = wrapper.getSecurityGroups()!!
                    "securityRules" -> dto = wrapper.getSecurityRules()
                }
                channel.sendResponse(
                    okJson(gson.toJson(dto))
                )
            } catch (e: Exception) {
                channel.sendResponse(err(RestStatus.BAD_GATEWAY, "compute.${metric}: ${e.message}"))
            }
        }
    }

    override fun documentation(sb: StringBuilder) {
        sb.append(
            """
                /nn2/compute/servers
                /nn2/compute/images
                /nn2/compute/flavors
                /nn2/compute/keypairs
                /nn2/compute/services
                /nn2/compute/floatingIps
                /nn2/compute/hosts
                /nn2/compute/zones
                /nn2/compute/migrations
                /nn2/compute/hypervisors
                /nn2/compute/hostAggregates
                /nn2/compute/serverGroups
                /nn2/compute/securityGroups
                /nn2/compute/securityRules
            """.trimIndent()
        )
    }

    override fun getTableWithHeader(rr: RestRequest?): Table? {
        return Table()
    }

    override fun getName(): String? = "compute_metric_handler"
}