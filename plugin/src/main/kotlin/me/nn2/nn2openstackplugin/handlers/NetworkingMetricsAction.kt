package me.nn2.nn2openstackplugin.handlers

import me.nn2.nn2openstackplugin.support.MessageHelper
import me.nn2.nn2openstackplugin.support.settings.GlobalSettings.Companion.NETWORKING_PATH
import me.nn2.nn2openstackplugin.support.OpenStackManager
import org.opensearch.client.node.NodeClient
import org.opensearch.common.Table
import org.opensearch.rest.RestChannel
import org.opensearch.rest.RestHandler
import org.opensearch.rest.RestRequest
import org.opensearch.rest.action.cat.AbstractCatAction

class NetworkingMetricsAction(private val manager: OpenStackManager) : AbstractCatAction() {
    override fun routes(): List<RestHandler.Route?>? {
        return listOf(
            RestHandler.Route(RestRequest.Method.GET, "${NETWORKING_PATH}/network"),
            RestHandler.Route(RestRequest.Method.GET, "${NETWORKING_PATH}/subnet"),
            RestHandler.Route(RestRequest.Method.GET, "${NETWORKING_PATH}/port"),
            RestHandler.Route(RestRequest.Method.GET, "${NETWORKING_PATH}/router"),
            RestHandler.Route(RestRequest.Method.GET, "${NETWORKING_PATH}/securitygroup"),
            RestHandler.Route(RestRequest.Method.GET, "${NETWORKING_PATH}/quotas"),
            RestHandler.Route(RestRequest.Method.GET, "${NETWORKING_PATH}/floatingip")
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
                var dto: Set<Any> = setOf()
                when (metric) {
                    "network" -> dto = wrapper.getNetworks().toSet()
                    "subnet" -> dto = wrapper.getSubnets().toSet()
                    "port" -> dto = wrapper.getPorts().toSet()
                    "router" -> dto = wrapper.getRouters().toSet()
                    "securitygroup" -> dto = wrapper.getSecurityGroups().toSet()
                    "quotas" -> dto = wrapper.getQuotas().toSet()
                    "floatingip" -> dto = wrapper.getFloatingIps().toSet()
                }
                MessageHelper.sendResponse(channel, dto)
            } catch (e: Exception) {
                MessageHelper.sendExceptionMessage(channel, e)
            }
        }
    }

    override fun documentation(sb: StringBuilder) {
        sb.append(
            """
                ${NETWORKING_PATH}/network
                ${NETWORKING_PATH}/subnet
                ${NETWORKING_PATH}/port
                ${NETWORKING_PATH}/router
                ${NETWORKING_PATH}/securitygroup
                ${NETWORKING_PATH}/quotas
                ${NETWORKING_PATH}/floatingip
            """.trimIndent()
        )
    }

    override fun getTableWithHeader(rr: RestRequest?): Table? {
        return Table()
    }

    override fun getName(): String? = "network_metric_handler"
}