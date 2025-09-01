package org.opensearch.openstackMetricHandler.action

import org.opensearch.common.Table
import org.opensearch.core.rest.RestStatus
import org.opensearch.openstackMetricHandler.config.OpenStackManager
import org.opensearch.openstackMetricHandler.config.err
import org.opensearch.openstackMetricHandler.config.gson
import org.opensearch.openstackMetricHandler.config.okJson
import org.opensearch.rest.RestHandler
import org.opensearch.rest.RestRequest
import org.opensearch.rest.action.cat.AbstractCatAction
import org.opensearch.transport.client.node.NodeClient

class BlockStorageMetricsHandlerAction(private val manager: OpenStackManager) : AbstractCatAction() {
    override fun routes(): List<RestHandler.Route?>? {
        return listOf(
            RestHandler.Route(RestRequest.Method.GET, "/nn2/blockStorage/volumes"),
            RestHandler.Route(RestRequest.Method.GET, "/nn2/blockStorage/backups"),
            RestHandler.Route(RestRequest.Method.GET, "/nn2/blockStorage/snapshots"),
            RestHandler.Route(RestRequest.Method.GET, "/nn2/blockStorage/services")
        )
    }

    override fun doCatRequest(
        rr: RestRequest?,
        nc: NodeClient?
    ): RestChannelConsumer? {
        val metric = rr?.path()?.split("/".toRegex())?.last()

        return RestChannelConsumer { channel ->
            try {
                val wrapper = manager.wrapper().blockStorage()
                var dto: Any = ""
                when (metric) {
                    "volumes" -> dto = wrapper.getVolumes()!!
                    "backups" -> dto = wrapper.getBackups()!!
                    "snapshots" -> dto = wrapper.getSnapshots()!!
                    "services" -> dto = wrapper.getServices()!!
                }
                channel.sendResponse(
                    okJson(gson.toJson(dto))
                )
            } catch (e: Exception) {
                channel.sendResponse(err(RestStatus.BAD_GATEWAY, "blockStorage.${metric}: ${e.message}"))
            }
        }
    }

    override fun documentation(sb: StringBuilder) {
        sb.append(
            """
                /nn2/blockStorage/volumes
                /nn2/blockStorage/backups
                /nn2/blockStorage/snapshots
                /nn2/blockStorage/services
            """.trimIndent()
        )
    }


    override fun getTableWithHeader(rr: RestRequest?): Table? {
        return Table()
    }

    override fun getName(): String? = "storage_metric_handler"
}