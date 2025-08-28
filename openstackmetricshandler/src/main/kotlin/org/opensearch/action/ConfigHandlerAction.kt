package org.opensearch.action

import org.opensearch.common.Table
import org.opensearch.config.*
import org.opensearch.core.rest.RestStatus
import org.opensearch.rest.RestHandler
import org.opensearch.rest.RestRequest
import org.opensearch.rest.action.cat.AbstractCatAction
import org.opensearch.transport.client.node.NodeClient

class ConfigHandlerAction(
    private val manager: OpenStackManager
) : AbstractCatAction() {
    override fun routes(): List<RestHandler.Route?>? {
        return listOf(
            RestHandler.Route(RestRequest.Method.GET, "/nn2/openstack/config"),
            RestHandler.Route(RestRequest.Method.POST, "/nn2/openstack/config")
        )
    }

    override fun doCatRequest(
        restRequest: RestRequest?,
        client: NodeClient?
    ): RestChannelConsumer? {
        when (restRequest?.method()) {
            RestRequest.Method.POST -> {
                val body: String = restRequest.content().utf8ToString().ifBlank { "{}" }
                val cfg: OpenStackConfig = try {
                    gson.fromJson(body, OpenStackConfig::class.java)
                } catch (e: Exception) {
                    return RestChannelConsumer { channel ->
                        channel.sendResponse(
                            err(
                                RestStatus.BAD_REQUEST,
                                "Invalid JSON: ${e.message}"
                            )
                        )
                    }
                }

                if (cfg.authUrl.isBlank() || cfg.username.isBlank() || cfg.password.isBlank()) {
                    return RestChannelConsumer { channel ->
                        channel.sendResponse(
                            err(
                                RestStatus.BAD_REQUEST,
                                "authUrl, username, password are required!"
                            )
                        )
                    }
                }
                return RestChannelConsumer { channel ->
                    try {
                        manager.setConfig(cfg)
                        channel.sendResponse(
                            okJson(
                                """{"result":"ok"}""".trimIndent()
                            )
                        )
                    } catch (e: Exception) {
                        channel.sendResponse(err(RestStatus.INTERNAL_SERVER_ERROR, e.message ?: "setConfig failed"))
                    }
                }
            }

            RestRequest.Method.GET -> {
                return RestChannelConsumer { channel ->
                    val cfg = manager.getConfig()
                    if (cfg == null) channel.sendResponse(err(RestStatus.NOT_FOUND, "Config not set"))
                    else channel.sendResponse(okJson(gson.toJson(cfg.copy(password = "***"))))
                }
            }

            RestRequest.Method.PUT -> TODO()
            RestRequest.Method.DELETE -> TODO()
            RestRequest.Method.OPTIONS -> TODO()
            RestRequest.Method.HEAD -> TODO()
            RestRequest.Method.PATCH -> TODO()
            RestRequest.Method.TRACE -> TODO()
            RestRequest.Method.CONNECT -> TODO()
            null -> TODO()
        }
    }

    override fun documentation(p0: java.lang.StringBuilder?) {
        TODO("Not yet implemented")
    }

    override fun getTableWithHeader(rr: RestRequest?): Table? {
        return Table()
    }

    override fun getName(): String? = "config_handler"
}