package me.nn2.nn2openstackplugin.handlers

import me.nn2.nn2openstackplugin.support.MessageHelper
import me.nn2.nn2openstackplugin.support.OpenStackManager
import me.nn2.nn2openstackplugin.support.settings.GlobalSettings
import org.opensearch.client.node.NodeClient
import org.opensearch.rest.BaseRestHandler
import org.opensearch.rest.RestHandler
import org.opensearch.rest.RestRequest

class BlockStorageMetricsAction(private val globalSettings: GlobalSettings) : BaseRestHandler() {
    override fun routes(): List<RestHandler.Route?>? {
        return listOf(
            RestHandler.Route(RestRequest.Method.GET, "${GlobalSettings.STORAGE_PATH}/volumes"),
            RestHandler.Route(RestRequest.Method.GET, "${GlobalSettings.STORAGE_PATH}/backups"),
            RestHandler.Route(RestRequest.Method.GET, "${GlobalSettings.STORAGE_PATH}/snapshots"),
            RestHandler.Route(RestRequest.Method.GET, "${GlobalSettings.STORAGE_PATH}/services")
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
        ).blockStorage()
        val metric = p0?.path()?.split("/".toRegex())?.last()

        return RestChannelConsumer { channel ->
            try {
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

    override fun getName(): String? = "storage_metric_handler"
}