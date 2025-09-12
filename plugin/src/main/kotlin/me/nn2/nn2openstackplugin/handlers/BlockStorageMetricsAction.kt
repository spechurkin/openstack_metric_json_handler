package me.nn2.nn2openstackplugin.handlers

import me.nn2.nn2openstackplugin.support.MessageHelper
import me.nn2.nn2openstackplugin.support.settings.GlobalSettings.Companion.STORAGE_PATH
import me.nn2.nn2openstackplugin.support.OpenStackManager
import org.opensearch.client.node.NodeClient
import org.opensearch.common.Table
import org.opensearch.rest.RestHandler
import org.opensearch.rest.RestRequest
import org.opensearch.rest.action.cat.AbstractCatAction

class BlockStorageMetricsAction(private val manager: OpenStackManager) : AbstractCatAction() {
    override fun routes(): List<RestHandler.Route?>? {
        return listOf(
            RestHandler.Route(RestRequest.Method.GET, "${STORAGE_PATH}/volumes"),
            RestHandler.Route(RestRequest.Method.GET, "${STORAGE_PATH}/backups"),
            RestHandler.Route(RestRequest.Method.GET, "${STORAGE_PATH}/snapshots"),
            RestHandler.Route(RestRequest.Method.GET, "${STORAGE_PATH}/services")
        )
    }

    override fun doCatRequest(
        p0: RestRequest?,
        p1: NodeClient?
    ): RestChannelConsumer? {
        val metric = p0?.path()?.split("/".toRegex())?.last()

        return RestChannelConsumer { channel ->
            try {
                val wrapper = manager.wrapper().blockStorage()
                var dto: Set<Any> = setOf()
                when (metric) {
                    "volumes" -> dto = wrapper.getVolumes().toSet()
                    "backups" -> dto = wrapper.getBackups().toSet()
                    "snapshots" -> dto = wrapper.getSnapshots().toSet()
                    "services" -> dto = wrapper.getServices().toSet()
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
                ${STORAGE_PATH}/volumes
                ${STORAGE_PATH}/backups
                ${STORAGE_PATH}/snapshots
                ${STORAGE_PATH}/services
            """.trimIndent()
        )
    }

    override fun getTableWithHeader(rr: RestRequest?): Table? {
        return Table()
    }

    override fun getName(): String? = "storage_metric_handler"
}