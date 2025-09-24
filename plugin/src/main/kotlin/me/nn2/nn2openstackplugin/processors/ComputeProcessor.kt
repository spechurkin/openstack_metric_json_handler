package me.nn2.nn2openstackplugin.processors

import me.nn2.libs.OpenStackWrapper
import me.nn2.nn2openstackplugin.requests.compute.FlavorRequest
import me.nn2.nn2openstackplugin.requests.compute.ImageRequest
import me.nn2.nn2openstackplugin.requests.compute.ServerRequest
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
            RestRequest.Method.GET -> {
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
                            else -> logger.warn("Bad request!")
                        }
                    }
                    MessageHelper.sendResponse(channel, dto)
                } catch (e: Exception) {
                    logger.error(e.message, e)
                    MessageHelper.sendExceptionMessage(channel, e)
                }
            }

            RestRequest.Method.POST -> with(request.path().lowercase(Locale.getDefault())) {
                when {
                    contains("servers/create") -> {
                        try {
                            val body: Map<String, Any> = request.contentParser().map()

                            val serverRequest = ServerRequest.Builder()
                                .serverName(body["serverName"].toString())
                                .imageName(body["imageName"].toString())
                                .flavorName(body["flavorName"].toString())
                                .networkNames(
                                    body["networkNames"].toString()
                                        .split(",").toList()
                                )
                                .adminPass(body["adminPass"].toString())
                                .apply {
                                    body["keyPair"]?.let { keyPair(it.toString()) }
                                }.build()

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

                    contains("images/create") -> {
                        try {
                            val body: Map<String, Any> = request.contentParser().map()

                            val imageRequest = ImageRequest.Builder()
                                .imageName(body["imageName"].toString())
                                .visibility(body["visibility"].toString())
                                .urlToImg(body["urlToImg"].toString())
                                .apply {
                                    body["minRamGb"]?.let { minRamGb(it as Int) }
                                    body["minDiskGb"]?.let { minDiskGb(it as Int) }
                                }.build()

                            compute.createImage(
                                imageName = imageRequest.imageName,
                                minRamGb = imageRequest.minRamGb,
                                minDiskGb = imageRequest.minDiskGb,
                                visibility = imageRequest.visibility,
                                urlToImg = imageRequest.urlToImg
                            )
                        } catch (e: Exception) {
                            logger.error(e.message, e)
                            MessageHelper.sendExceptionMessage(channel, e)
                        }
                    }

                    contains("flavors/create") -> {
                        try {
                            val body: Map<String, Any> = request.contentParser().map()

                            val flavorRequest = FlavorRequest.Builder()
                                .flavorName(body["flavorName"].toString())
                                .ramMb(body["ramMb"] as Int)
                                .diskGb(body["diskGb"] as Int)
                                .vcpus(body["vcpus"] as Int)
                                .apply {
                                    body["ephemeralGb"]?.let { ephemeralGb(it as Int) }
                                    body["swapMb"]?.let { swapMb(it as Int) }
                                    body["rxtxFactor"]?.let { rxtxFactor(it as Double) }
                                    body["isPublic"]?.let { isPublic(it as Boolean) }
                                }.build()

                            compute.createFlavor(
                                flavorName = flavorRequest.flavorName,
                                ramMb = flavorRequest.ramMb,
                                diskGb = flavorRequest.diskGb,
                                ephemeralGb = flavorRequest.ephemeralGb,
                                swapMb = flavorRequest.swapMb,
                                vcpus = flavorRequest.vcpus,
                                rxtxFactor = flavorRequest.rxtxFactor,
                                isPublic = flavorRequest.isPublic
                            )
                        } catch (e: Exception) {
                            logger.error(e.message, e)
                            MessageHelper.sendExceptionMessage(channel, e)
                        }
                    }

                    else -> logger.warn("Bad request!")
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