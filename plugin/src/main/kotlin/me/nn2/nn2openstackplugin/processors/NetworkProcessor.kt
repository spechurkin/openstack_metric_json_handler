package me.nn2.nn2openstackplugin.processors

import me.nn2.libs.OpenStackWrapper
import me.nn2.nn2openstackplugin.requests.networking.NetworkRequest
import me.nn2.nn2openstackplugin.requests.networking.SubnetRequest
import me.nn2.nn2openstackplugin.support.MessageHelper
import org.apache.logging.log4j.LogManager
import org.opensearch.rest.RestChannel
import org.opensearch.rest.RestRequest

class NetworkProcessor() : IProcessor {
    private val logger = LogManager.getLogger(NetworkProcessor::class.java)

    @Throws
    override fun process(wrapper: OpenStackWrapper, channel: RestChannel, request: RestRequest?) {
        val networking = wrapper.networking()

        when (request?.method()) {
            RestRequest.Method.GET -> {
                try {
                    var dto: Set<Any> = setOf()
                    with(request.path()) {
                        when {
                            contains("networks") -> dto = networking.getNetworks().toSet()
                            contains("subnets") -> dto = networking.getSubnets().toSet()
                            contains("ports") -> dto = networking.getPorts().toSet()
                            contains("routers") -> dto = networking.getRouters().toSet()
                            contains("securityGroups") -> dto = networking.getSecurityGroups().toSet()
                            contains("securityRules") -> dto = networking.getSecurityGroupRules().toSet()
                            contains("floatingIps") -> dto = networking.getFloatingIps().toSet()
                            else -> logger.warn("Bad request!")
                        }
                    }
                    MessageHelper.sendResponse(channel, dto)
                } catch (e: Exception) {
                    logger.error(e.message, e)
                    MessageHelper.sendExceptionMessage(channel, e)
                }
            }

            RestRequest.Method.POST -> with(request.path()) {
                when {
                    contains("networks/create") -> {
                        try {
                            val body: Map<String, Any> = request.contentParser().map()

                            val networkRequest = NetworkRequest.Builder()
                                .networkName(body["networkName"].toString())
                                .projectName(body["projectName"].toString())
                                .networkType(body["networkType"].toString())
                                .apply {
                                    body["isAdmin"]?.let { isAdmin(it as Boolean) }
                                    body["isShared"]?.let { isShared(it as Boolean) }
                                    body["isExternal"]?.let { isExternal(it as Boolean) }
                                    body["physicalNetwork"]?.let { physicalNetwork(it.toString()) }
                                    body["segmentId"]?.let { segmentId(it.toString()) }
                                }.build()

                            networking.createNetwork(
                                projectName = networkRequest.projectName,
                                networkName = networkRequest.networkName,
                                isShared = networkRequest.isShared,
                                isExternal = networkRequest.isExternal,
                                isAdmin = networkRequest.isAdmin,
                                networkType = networkRequest.networkType,
                                physicalNetwork = networkRequest.physicalNetwork,
                                segmentId = networkRequest.segmentId
                            )

                        } catch (e: Exception) {
                            logger.error(e.message, e)
                            MessageHelper.sendExceptionMessage(channel, e)
                        }
                    }

                    contains("subnets/create") -> {
                        try {
                            val body: Map<String, Any> = request.contentParser().map()

                            val subnetRequest = SubnetRequest.Builder()
                                .subnetName(body["subnetName"].toString())
                                .networkName(body["networkName"].toString())
                                .apply {
                                    body["ipVersion"]?.let { ipVersion(it.toString()) }
                                    body["raMode"]?.let { raMode(it.toString()) }
                                    body["addressMode"]?.let { addressMode(it.toString()) }
                                    body["cidr"]?.let { cidr(it.toString()) }
                                    body["gateway"]?.let { gateway(it.toString()) }
                                    body["start"]?.let { start(it.toString()) }
                                    body["end"]?.let { end(it.toString()) }
                                    body["destination"]?.let { destination(it.toString()) }
                                    body["nextHop"]?.let { nextHop(it.toString()) }
                                    body["dnsServer"]?.let { dnsServer(it.toString()) }
                                }.build()

                            networking.addSubnet(
                                subnetName = subnetRequest.subnetName,
                                networkName = subnetRequest.networkName,
                                ipVersion = subnetRequest.ipVersion,
                                raMode = subnetRequest.raMode,
                                addressMode = subnetRequest.addressMode,
                                cidr = subnetRequest.cidr,
                                gateway = subnetRequest.gateway,
                                start = subnetRequest.start,
                                end = subnetRequest.end,
                                destination = subnetRequest.destination,
                                nextHop = subnetRequest.nextHop,
                                dnsServer = subnetRequest.dnsServer
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