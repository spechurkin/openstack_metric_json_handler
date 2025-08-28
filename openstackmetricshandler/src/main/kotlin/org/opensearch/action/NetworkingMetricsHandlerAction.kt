package org.opensearch.action

import org.opensearch.common.Table
import org.opensearch.config.OpenStackManager
import org.opensearch.config.err
import org.opensearch.config.gson
import org.opensearch.config.okJson
import org.opensearch.core.rest.RestStatus
import org.opensearch.rest.RestChannel
import org.opensearch.rest.RestHandler
import org.opensearch.rest.RestRequest
import org.opensearch.rest.action.cat.AbstractCatAction
import org.opensearch.transport.client.node.NodeClient

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
        rr: RestRequest?,
        nc: NodeClient?
    ): RestChannelConsumer? {
        val metric = rr?.path()?.split("/".toRegex())?.last()

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

    override fun documentation(p0: java.lang.StringBuilder?) {
        TODO("Not yet implemented")
    }

    override fun getTableWithHeader(rr: RestRequest?): Table? {
        return Table()
    }

    override fun getName(): String? = "storage_metric_handler"
}