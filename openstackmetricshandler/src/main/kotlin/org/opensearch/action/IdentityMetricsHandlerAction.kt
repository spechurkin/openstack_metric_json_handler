package org.opensearch.action

import org.opensearch.common.Table
import org.opensearch.config.OpenStackManager
import org.opensearch.config.err
import org.opensearch.config.okJson
import org.opensearch.core.rest.RestStatus
import org.opensearch.rest.RestHandler
import org.opensearch.rest.RestRequest
import org.opensearch.rest.action.cat.AbstractCatAction
import org.opensearch.transport.client.node.NodeClient

class IdentityMetricsHandlerAction(private val manager: OpenStackManager) : AbstractCatAction() {
    override fun routes(): List<RestHandler.Route?>? {
        return listOf(
            RestHandler.Route(RestRequest.Method.GET, "/nn2/identity/volumes"),
            RestHandler.Route(RestRequest.Method.GET, "/nn2/identity/backups"),
            RestHandler.Route(RestRequest.Method.GET, "/nn2/identity/snapshots"),
            RestHandler.Route(RestRequest.Method.GET, "/nn2/identity/domains")
        )
    }

    override fun doCatRequest(
        rr: RestRequest?,
        nc: NodeClient?
    ): RestChannelConsumer? {
        val metric = rr?.path()?.split("/".toRegex())?.last()

        return RestChannelConsumer { channel ->
            try {
                val wrapper = manager.wrapper().identity()
                var dto: Any = ""
                when (metric) {
                    "volumes" -> dto = wrapper.getUsers()
                    "backups" -> dto = wrapper.getGroups()
                    "snapshots" -> dto = wrapper.getProjects()
                    "domains" -> dto = wrapper.getDomains()
                }
                channel.sendResponse(
                    okJson(dto.toString())
                )
            } catch (e: Exception) {
                channel.sendResponse(err(RestStatus.BAD_GATEWAY, "identity.${metric}: ${e.message}"))
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