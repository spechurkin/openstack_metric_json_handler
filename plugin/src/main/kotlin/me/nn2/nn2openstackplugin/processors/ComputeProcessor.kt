package me.nn2.nn2openstackplugin.processors

import me.nn2.libs.OpenStackWrapper
import me.nn2.nn2openstackplugin.requests.ServerRequest
import me.nn2.nn2openstackplugin.support.MessageHelper
import org.apache.logging.log4j.LogManager
import org.opensearch.rest.RestChannel
import org.opensearch.rest.RestRequest
import java.util.*

class ComputeProcessor() : IProcessor {
    private val logger = LogManager.getLogger(ComputeProcessor::class.java)

    @Throws
    override fun process(wrapper: OpenStackWrapper, channel: RestChannel, request: RestRequest?) {
        val compute = wrapper.compute()
        when (request?.method()) {
            RestRequest.Method.GET ->
                try {
                    var dto: Set<Any> = setOf()
                    with(request.path()?.lowercase(Locale.getDefault())!!) {
                        when {
                            contains("servers") -> dto = compute.getServers().toSet()
                            contains("images") -> dto = compute.getImages().toSet()
                            contains("flavors") -> dto = compute.getFlavors().toSet()
                            contains("keypairs") -> dto = compute.getKeypairs().toSet()
                            contains("services") -> dto = compute.getServices().toSet()
                            contains("floatingIps") -> dto = compute.getFloatingIps().toSet()
                            contains("hosts") -> dto = compute.getHosts().toSet()
                            contains("zones") -> dto = compute.getZones().toSet()
                            contains("migrations") -> dto = compute.getMigrations().toSet()
                            contains("hypervisors") -> dto = compute.getHypervisors().toSet()
                            contains("hostAggregates") -> dto = compute.getHostAggregates().toSet()
                            contains("serverGroups") -> dto = compute.getServerGroups().toSet()
                            contains("securityGroups") -> dto = compute.getSecurityGroups().toSet()
                            contains("securityRules") -> dto = compute.getSecurityRules().toSet()
                        }
                    }
                    MessageHelper.sendResponse(channel, dto)
                } catch (e: Exception) {
                    logger.error(e.message, e)
                    MessageHelper.sendExceptionMessage(channel, e)
                }

            RestRequest.Method.POST ->
                with(request.path().lowercase(Locale.getDefault())) {
                    when {
                        contains("servers/create") -> {
                            try {
                                val serverRequestBuilder = ServerRequest.Builder()
                                    .serverName(request.param("serverName"))
                                    .imageName(request.param("imageName"))
                                    .flavorName(request.param("flavorName"))

                                if (request.params().containsKey("adminPass")) {
                                    serverRequestBuilder.adminPass(request.param("adminPass"))
                                }

                                if (request.params().containsKey("keyPair")) {
                                    serverRequestBuilder.keyPair(request.param("keyPair"))
                                }

                                if (request.params().containsKey("networkNames")) {
                                    serverRequestBuilder.networkNames(request.param("networkNames").split(","))
                                } else {
                                    serverRequestBuilder.networkNames(emptyList())
                                }

                                val serverRequest = serverRequestBuilder.build()

                                compute.createServer(
                                    serverName = serverRequest.serverName,
                                    imageName = serverRequest.imageName,
                                    flavorName = serverRequest.flavorName,
                                    adminPass = serverRequest.adminPass,
                                    keyPair = serverRequest.keyPair,
                                    networkNames = serverRequest.networkNames
                                )
                            } catch (e: Exception) {
                                logger.error(e.message, e)
                                MessageHelper.sendExceptionMessage(channel, e)
                            }
                        }
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
}