package me.nn2.nn2openstackplugin.handlers

import me.nn2.nn2openstackplugin.support.MessageHelper
import me.nn2.nn2openstackplugin.support.OpenStackManager
import me.nn2.nn2openstackplugin.support.settings.GlobalSettings.Companion.IDENTITY_PATH
import org.opensearch.client.node.NodeClient
import org.opensearch.common.Table
import org.opensearch.rest.RestHandler
import org.opensearch.rest.RestRequest
import org.opensearch.rest.action.cat.AbstractCatAction

class IdentityMetricsAction(private val manager: OpenStackManager) : AbstractCatAction() {
    override fun routes(): List<RestHandler.Route?>? {
        return listOf(
            RestHandler.Route(RestRequest.Method.GET, "${IDENTITY_PATH}/volumes"),
            RestHandler.Route(RestRequest.Method.GET, "${IDENTITY_PATH}/backups"),
            RestHandler.Route(RestRequest.Method.GET, "${IDENTITY_PATH}/snapshots"),
            RestHandler.Route(RestRequest.Method.GET, "${IDENTITY_PATH}/domains")
        )
    }

    override fun doCatRequest(
        p0: RestRequest?,
        p1: NodeClient?
    ): RestChannelConsumer? {
        val metric = p0?.path()?.split("/".toRegex())?.last()

        return RestChannelConsumer { channel ->
            try {
                val wrapper = manager.wrapper().identity()
                var dto = ""
                when (metric) {
                    "volumes" -> dto = wrapper.getUsers()
                    "backups" -> dto = wrapper.getGroups()
                    "snapshots" -> dto = wrapper.getProjects()
                    "domains" -> dto = wrapper.getDomains()
                }
                MessageHelper.sendMessage(channel, dto)
            } catch (e: Exception) {
                MessageHelper.sendExceptionMessage(channel, e)
            }
        }
    }

    override fun documentation(sb: StringBuilder) {
        sb.append(
            """
                ${IDENTITY_PATH}/volumes
                ${IDENTITY_PATH}/backups
                ${IDENTITY_PATH}/snapshots
                ${IDENTITY_PATH}/domains
            """.trimIndent()
        )
    }

    override fun getTableWithHeader(rr: RestRequest?): Table? {
        return Table()
    }

    override fun getName(): String? = "identity_metric_handler"
}