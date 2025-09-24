package me.nn2.nn2openstackplugin.handlers

import me.nn2.libs.OpenStackWrapper
import me.nn2.nn2openstackplugin.processors.IdentityProcessor
import me.nn2.nn2openstackplugin.support.MessageHelper
import me.nn2.nn2openstackplugin.support.settings.GlobalSettings
import org.opensearch.client.node.NodeClient
import org.opensearch.rest.BaseRestHandler
import org.opensearch.rest.RestHandler
import org.opensearch.rest.RestRequest
import org.slf4j.LoggerFactory

class IdentityMetricsAction(private val wrapper: OpenStackWrapper) : BaseRestHandler() {
    private val log = LoggerFactory.getLogger(ComputeMetricsAction::class.java)

    override fun routes(): List<RestHandler.Route?>? {
        return listOf(
            RestHandler.Route(RestRequest.Method.GET, "${GlobalSettings.IDENTITY_PATH}/users"),
            RestHandler.Route(RestRequest.Method.GET, "${GlobalSettings.IDENTITY_PATH}/groups"),
            RestHandler.Route(RestRequest.Method.GET, "${GlobalSettings.IDENTITY_PATH}/projects"),
            RestHandler.Route(RestRequest.Method.GET, "${GlobalSettings.IDENTITY_PATH}/domains")
        )
    }

    override fun prepareRequest(
        p0: RestRequest?,
        p1: NodeClient?
    ): RestChannelConsumer? {
        val processor = IdentityProcessor()

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

    override fun getName(): String? = "identity_metric_handler"
}