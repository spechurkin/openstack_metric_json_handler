package me.nn2.nn2openstackplugin.handlers

import me.nn2.nn2openstackplugin.support.MessageHelper
import me.nn2.nn2openstackplugin.support.settings.GlobalSettings.Companion.COMPUTE_PATH
import me.nn2.nn2openstackplugin.support.OpenStackManager
import org.opensearch.client.node.NodeClient
import org.opensearch.common.Table
import org.opensearch.rest.RestChannel
import org.opensearch.rest.RestHandler
import org.opensearch.rest.RestRequest
import org.opensearch.rest.action.cat.AbstractCatAction

class ComputeMetricsAction(private val manager: OpenStackManager) : AbstractCatAction() {
    override fun routes(): List<RestHandler.Route?>? {
        return listOf(
            RestHandler.Route(RestRequest.Method.GET, "${COMPUTE_PATH}/servers"),
            RestHandler.Route(RestRequest.Method.GET, "${COMPUTE_PATH}/images"),
            RestHandler.Route(RestRequest.Method.GET, "${COMPUTE_PATH}/flavors"),
            RestHandler.Route(RestRequest.Method.GET, "${COMPUTE_PATH}/keypairs"),
            RestHandler.Route(RestRequest.Method.GET, "${COMPUTE_PATH}/services"),
            RestHandler.Route(RestRequest.Method.GET, "${COMPUTE_PATH}/floatingIps"),
            RestHandler.Route(RestRequest.Method.GET, "${COMPUTE_PATH}/hosts"),
            RestHandler.Route(RestRequest.Method.GET, "${COMPUTE_PATH}/zones"),
            RestHandler.Route(RestRequest.Method.GET, "${COMPUTE_PATH}/migrations"),
            RestHandler.Route(RestRequest.Method.GET, "${COMPUTE_PATH}/hypervisors"),
            RestHandler.Route(RestRequest.Method.GET, "${COMPUTE_PATH}/hostAggregates"),
            RestHandler.Route(RestRequest.Method.GET, "${COMPUTE_PATH}/serverGroups"),
            RestHandler.Route(RestRequest.Method.GET, "${COMPUTE_PATH}/securityGroups"),
            RestHandler.Route(RestRequest.Method.GET, "${COMPUTE_PATH}/securityRules")
        )
    }

    override fun doCatRequest(
        p0: RestRequest?,
        p1: NodeClient?
    ): RestChannelConsumer? {
        val metric = p0?.path()?.split("/".toRegex())?.last()

        return RestChannelConsumer { channel: RestChannel ->
            try {
                val wrapper = manager.wrapper().compute()
                var dto: Set<Any> = setOf()
                when (metric) {
                    "servers" -> dto = wrapper.getServers().toSet()
                    "images" -> dto = wrapper.getImages().toSet()
                    "flavors" -> dto = wrapper.getFlavors().toSet()
                    "keypairs" -> dto = wrapper.getKeypairs().toSet()
                    "services" -> dto = wrapper.getServices().toSet()
                    "floatingIps" -> dto = wrapper.getFloatingIps().toSet()
                    "hosts" -> dto = wrapper.getHosts().toSet()
                    "zones" -> dto = wrapper.getZones().toSet()
                    "migrations" -> dto = wrapper.getMigrations().toSet()
                    "hypervisors" -> dto = wrapper.getHypervisors().toSet()
                    "hostAggregates" -> dto = wrapper.getHostAggregates().toSet()
                    "serverGroups" -> dto = wrapper.getServerGroups().toSet()
                    "securityGroups" -> dto = wrapper.getSecurityGroups().toSet()
                    "securityRules" -> dto = wrapper.getSecurityRules().toSet()
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
                ${COMPUTE_PATH}/servers
                ${COMPUTE_PATH}/images
                ${COMPUTE_PATH}/flavors
                ${COMPUTE_PATH}/keypairs
                ${COMPUTE_PATH}/services
                ${COMPUTE_PATH}/floatingIps
                ${COMPUTE_PATH}/hosts
                ${COMPUTE_PATH}/zones
                ${COMPUTE_PATH}/migrations
                ${COMPUTE_PATH}/hypervisors
                ${COMPUTE_PATH}/hostAggregates
                ${COMPUTE_PATH}/serverGroups
                ${COMPUTE_PATH}/securityGroups
                ${COMPUTE_PATH}/securityRules
            """.trimIndent()
        )
    }

    override fun getTableWithHeader(rr: RestRequest?): Table? {
        return Table()
    }

    override fun getName(): String? = "compute_metric_handler"
}