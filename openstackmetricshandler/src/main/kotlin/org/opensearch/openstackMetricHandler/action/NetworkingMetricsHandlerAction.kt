package org.opensearch.openstackMetricHandler.action

import org.opensearch.client.node.NodeClient
import org.opensearch.common.Table
import org.opensearch.core.rest.RestStatus
import org.opensearch.openstackMetricHandler.config.OpenStackManager
import org.opensearch.openstackMetricHandler.config.err
import org.opensearch.openstackMetricHandler.config.gson
import org.opensearch.openstackMetricHandler.config.okJson
import org.opensearch.rest.RestChannel
import org.opensearch.rest.RestHandler
import org.opensearch.rest.RestRequest
import org.opensearch.rest.action.cat.AbstractCatAction

class NetworkingMetricsHandlerAction(private val manager: OpenStackManager) : AbstractCatAction() {
    override fun routes(): List<RestHandler.Route?>? {
        return listOf(
            RestHandler.Route(RestRequest.Method.GET, "/nn2/networking/network"),
            RestHandler.Route(RestRequest.Method.GET, "/nn2/networking/subnet"),
            RestHandler.Route(RestRequest.Method.GET, "/nn2/networking/port"),
            RestHandler.Route(RestRequest.Method.GET, "/nn2/networking/router"),
            RestHandler.Route(RestRequest.Method.GET, "/nn2/networking/securitygroup"),
            RestHandler.Route(RestRequest.Method.GET, "/nn2/networking/quotas"),
            RestHandler.Route(RestRequest.Method.GET, "/nn2/networking/floatingip")
        )
    }

    override fun doCatRequest(
        p0: RestRequest?,
        p1: NodeClient?
    ): RestChannelConsumer? {
        val metric = p0?.path()?.split("/".toRegex())?.last()

        return RestChannelConsumer { channel: RestChannel ->
            try {
                val wrapper = manager.wrapper().networking()
                var dto: Any = ""
                when (metric) {
                    "network" -> dto = wrapper.getNetworks()!!
                    "subnet" -> dto = wrapper.getSubnets()!!
                    "port" -> dto = wrapper.getPorts()!!
                    "router" -> dto = wrapper.getRouters()!!
                    "securitygroup" -> dto = wrapper.getSecurityGroups()!!
                    "quotas" -> dto = wrapper.getQuotas()!!
                    "floatingip" -> dto = wrapper.getFloatingIps()!!
                }
                channel.sendResponse(
                    okJson(gson.toJson(dto))
                )
            } catch (e: Exception) {
                channel.sendResponse(err(RestStatus.BAD_GATEWAY, "networking.${metric}: ${e.message}"))
            }
        }
    }

    override fun documentation(sb: StringBuilder) {
        sb.append(
            """
                /nn2/networking/network
                /nn2/networking/subnet
                /nn2/networking/port
                /nn2/networking/router
                /nn2/networking/securitygroup
                /nn2/networking/quotas
                /nn2/networking/floatingip
            """.trimIndent()
        )
    }

    override fun getTableWithHeader(rr: RestRequest?): Table? {
        return Table()
    }

    override fun getName(): String? = "network_metric_handler"
}