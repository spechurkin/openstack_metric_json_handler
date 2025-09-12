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
        val metric = p0?.path()?.split("/".toRegex())?.last()
        val processor = NetworkProcessor()

        return try {
            RestChannelConsumer {
                try {
                    processor.process(metric!!, wrapper, it)
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