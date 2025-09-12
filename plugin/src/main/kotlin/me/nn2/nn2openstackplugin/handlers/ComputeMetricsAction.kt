package me.nn2.nn2openstackplugin.handlers

import me.nn2.nn2openstackplugin.support.MessageHelper
import me.nn2.nn2openstackplugin.support.OpenStackManager
import me.nn2.nn2openstackplugin.support.settings.GlobalSettings
import org.opensearch.client.node.NodeClient
import org.opensearch.rest.BaseRestHandler
import org.opensearch.rest.RestChannel
import org.opensearch.rest.RestHandler
import org.opensearch.rest.RestRequest

class ComputeMetricsAction(private val globalSettings: GlobalSettings) : BaseRestHandler() {
    override fun routes(): List<RestHandler.Route?>? {
        return listOf(
            RestHandler.Route(RestRequest.Method.GET, "${GlobalSettings.COMPUTE_PATH}/servers"),
            RestHandler.Route(RestRequest.Method.GET, "${GlobalSettings.COMPUTE_PATH}/images"),
            RestHandler.Route(RestRequest.Method.GET, "${GlobalSettings.COMPUTE_PATH}/flavors"),
            RestHandler.Route(RestRequest.Method.GET, "${GlobalSettings.COMPUTE_PATH}/keypairs"),
            RestHandler.Route(RestRequest.Method.GET, "${GlobalSettings.COMPUTE_PATH}/services"),
            RestHandler.Route(RestRequest.Method.GET, "${GlobalSettings.COMPUTE_PATH}/floatingIps"),
            RestHandler.Route(RestRequest.Method.GET, "${GlobalSettings.COMPUTE_PATH}/hosts"),
            RestHandler.Route(RestRequest.Method.GET, "${GlobalSettings.COMPUTE_PATH}/zones"),
            RestHandler.Route(RestRequest.Method.GET, "${GlobalSettings.COMPUTE_PATH}/migrations"),
            RestHandler.Route(RestRequest.Method.GET, "${GlobalSettings.COMPUTE_PATH}/hypervisors"),
            RestHandler.Route(RestRequest.Method.GET, "${GlobalSettings.COMPUTE_PATH}/hostAggregates"),
            RestHandler.Route(RestRequest.Method.GET, "${GlobalSettings.COMPUTE_PATH}/serverGroups"),
            RestHandler.Route(RestRequest.Method.GET, "${GlobalSettings.COMPUTE_PATH}/securityGroups"),
            RestHandler.Route(RestRequest.Method.GET, "${GlobalSettings.COMPUTE_PATH}/securityRules")
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
        ).compute()
        val metric = p0?.path()?.split("/".toRegex())?.last()

        return RestChannelConsumer { channel: RestChannel ->
            try {
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

    override fun getName(): String? = "compute_metric_handler"
}