package me.nn2.nn2openstackplugin.handlers

import me.nn2.libs.OpenStackWrapper
import me.nn2.nn2openstackplugin.processors.ComputeProcessor
import me.nn2.nn2openstackplugin.support.MessageHelper
import me.nn2.nn2openstackplugin.support.settings.GlobalSettings
import org.opensearch.client.node.NodeClient
import org.opensearch.rest.BaseRestHandler
import org.opensearch.rest.RestHandler
import org.opensearch.rest.RestRequest
import org.slf4j.LoggerFactory

class ComputeMetricsAction(private val wrapper: OpenStackWrapper) :
    BaseRestHandler() {
    private val log = LoggerFactory.getLogger(ComputeMetricsAction::class.java)

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
        p0: RestRequest?, p1: NodeClient?
    ): RestChannelConsumer? {
        val metric = p0?.path()?.split("/".toRegex())?.last()
        val processor = ComputeProcessor()

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

    override fun getName(): String? = "compute_metric_handler"
}