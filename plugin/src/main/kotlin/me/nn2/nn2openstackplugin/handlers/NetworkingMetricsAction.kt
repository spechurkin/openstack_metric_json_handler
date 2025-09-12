package me.nn2.nn2openstackplugin.handlers

import me.nn2.nn2openstackplugin.support.MessageHelper
import me.nn2.nn2openstackplugin.support.OpenStackManager
import me.nn2.nn2openstackplugin.support.settings.GlobalSettings
import org.opensearch.client.node.NodeClient
import org.opensearch.rest.BaseRestHandler
import org.opensearch.rest.RestChannel
import org.opensearch.rest.RestHandler
import org.opensearch.rest.RestRequest

class NetworkingMetricsAction(private val globalSettings: GlobalSettings) : BaseRestHandler() {
    override fun routes(): List<RestHandler.Route?>? {
        return listOf(
            RestHandler.Route(RestRequest.Method.GET, "${GlobalSettings.NETWORKING_PATH}/network"),
            RestHandler.Route(RestRequest.Method.GET, "${GlobalSettings.NETWORKING_PATH}/subnet"),
            RestHandler.Route(RestRequest.Method.GET, "${GlobalSettings.NETWORKING_PATH}/port"),
            RestHandler.Route(RestRequest.Method.GET, "${GlobalSettings.NETWORKING_PATH}/router"),
            RestHandler.Route(RestRequest.Method.GET, "${GlobalSettings.NETWORKING_PATH}/securitygroup"),
            RestHandler.Route(RestRequest.Method.GET, "${GlobalSettings.NETWORKING_PATH}/quotas"),
            RestHandler.Route(RestRequest.Method.GET, "${GlobalSettings.NETWORKING_PATH}/floatingip")
        )
    }

    override fun prepareRequest(
        p0: RestRequest?,
        p1: NodeClient?
    ): RestChannelConsumer? {
        val wrapper = OpenStackManager().wrapper(
            authUrl = globalSettings.authUrl,
            username = globalSettings.openstackUser,
            password = globalSettings.openstackPassword,
            domain = globalSettings.domain,
            project = globalSettings.project,
            allowInsecure = globalSettings.allowInsecure
        ).networking()
        val metric = p0?.path()?.split("/".toRegex())?.last()

        return RestChannelConsumer { channel: RestChannel ->
            try {
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

    override fun getName(): String? = "network_metric_handler"
}