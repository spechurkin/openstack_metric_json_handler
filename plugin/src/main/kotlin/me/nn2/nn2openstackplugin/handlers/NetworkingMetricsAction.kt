package me.nn2.nn2openstackplugin.handlers

import me.nn2.libs.OpenStackWrapper
import me.nn2.nn2openstackplugin.processors.NetworkProcessor
import me.nn2.nn2openstackplugin.support.MessageHelper
import me.nn2.nn2openstackplugin.support.settings.GlobalSettings
import org.opensearch.client.node.NodeClient
import org.opensearch.rest.BaseRestHandler
import org.opensearch.rest.RestHandler
import org.opensearch.rest.RestRequest
import org.slf4j.LoggerFactory

class NetworkingMetricsAction(private val wrapper: OpenStackWrapper) : BaseRestHandler() {
    private val log = LoggerFactory.getLogger(ComputeMetricsAction::class.java)

    override fun routes(): List<RestHandler.Route?>? {
        return listOf(
            RestHandler.Route(RestRequest.Method.GET, "${GlobalSettings.NETWORKING_PATH}/networks"),
            RestHandler.Route(RestRequest.Method.POST, "${GlobalSettings.NETWORKING_PATH}/networks/create"),

            RestHandler.Route(RestRequest.Method.GET, "${GlobalSettings.NETWORKING_PATH}/subnets"),
            RestHandler.Route(RestRequest.Method.POST, "${GlobalSettings.NETWORKING_PATH}/subnets/create"),

            RestHandler.Route(RestRequest.Method.GET, "${GlobalSettings.NETWORKING_PATH}/ports"),
            RestHandler.Route(RestRequest.Method.GET, "${GlobalSettings.NETWORKING_PATH}/routers"),
            RestHandler.Route(RestRequest.Method.GET, "${GlobalSettings.NETWORKING_PATH}/securityGroups"),
            RestHandler.Route(RestRequest.Method.GET, "${GlobalSettings.NETWORKING_PATH}/securityRules"),
            RestHandler.Route(RestRequest.Method.GET, "${GlobalSettings.NETWORKING_PATH}/floatingIps")
        )
    }

    override fun prepareRequest(
        p0: RestRequest?,
        p1: NodeClient?
    ): RestChannelConsumer? {
        val processor = NetworkProcessor()

        return try {
            RestChannelConsumer {
                try {
                    processor.process(wrapper, it, p0)
                } catch (e: Exception) {
                    MessageHelper.sendExceptionMessage(it, e)
                }
            }
        } catch (e: IllegalStateException) {
            log.error(e.message)
            RestChannelConsumer { channel -> MessageHelper.sendExceptionMessage(channel, e) }
        }
    }

    override fun getName(): String? = "network_metric_handler"
}