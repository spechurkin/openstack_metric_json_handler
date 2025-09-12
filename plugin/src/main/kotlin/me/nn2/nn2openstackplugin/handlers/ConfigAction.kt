package me.nn2.nn2openstackplugin.handlers

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import me.nn2.nn2openstackplugin.support.MessageHelper
import me.nn2.nn2openstackplugin.support.settings.GlobalSettings.Companion.CONFIG_PATH
import me.nn2.nn2openstackplugin.support.settings.OpenStackConfig
import me.nn2.nn2openstackplugin.support.OpenStackManager
import org.opensearch.client.node.NodeClient
import org.opensearch.common.Table
import org.opensearch.rest.RestHandler
import org.opensearch.rest.RestRequest
import org.opensearch.rest.action.cat.AbstractCatAction

class ConfigAction(
    private val manager: OpenStackManager
) : AbstractCatAction() {
    private val gson: Gson = GsonBuilder().serializeNulls().setPrettyPrinting().disableHtmlEscaping().create()

    override fun routes(): List<RestHandler.Route?>? {
        return listOf(
            RestHandler.Route(RestRequest.Method.GET, CONFIG_PATH),
            RestHandler.Route(RestRequest.Method.POST, CONFIG_PATH)
        )
    }

    override fun doCatRequest(
        p0: RestRequest?,
        p1: NodeClient?
    ): RestChannelConsumer? {
        when (p0?.method()) {
            RestRequest.Method.POST -> {
                val body: String = p0.content().utf8ToString().ifBlank { "{}" }
                val cfg: OpenStackConfig = try {
                    gson.fromJson(body, OpenStackConfig::class.java)
                } catch (e: Exception) {
                    return RestChannelConsumer { channel ->
                        MessageHelper.sendExceptionMessage(channel, e)
                    }
                }

                if (cfg.authUrl.isBlank() || cfg.username.isBlank() || cfg.password.isBlank()) {
                    return RestChannelConsumer { channel ->
                        MessageHelper.sendMessage(channel, "authUrl, username, password are required!")
                    }
                }
                return RestChannelConsumer { channel ->
                    try {
                        manager.setConfig(cfg)
                        MessageHelper.sendMessage(channel, "Authorized!")
                    } catch (e: Exception) {
                        MessageHelper.sendExceptionMessage(channel, e)
                    }
                }
            }

            RestRequest.Method.HEAD,
            RestRequest.Method.GET,
            RestRequest.Method.PUT,
            RestRequest.Method.PATCH,
            RestRequest.Method.CONNECT -> {
                return RestChannelConsumer { channel ->
                    val cfg = manager.getConfig()
                    if (cfg == null) MessageHelper.sendMessage(channel, "Config not set")
                    else MessageHelper.sendResponse(channel, setOf(cfg.copy(password = "***")))
                }
            }

            RestRequest.Method.DELETE,
            RestRequest.Method.OPTIONS,
            RestRequest.Method.TRACE,
            null -> {
                return RestChannelConsumer { channel ->
                    MessageHelper.sendMessage(channel, "Config not set")
                }
            }
        }
    }

    override fun documentation(sb: StringBuilder) {
        sb.append(
            """
                $CONFIG_PATH
            """.trimIndent()
        )
    }

    override fun getTableWithHeader(rr: RestRequest?): Table? {
        return Table()
    }

    override fun getName(): String? = "config_handler"
}