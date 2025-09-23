package me.nn2.nn2openstackplugin.processors

import me.nn2.libs.OpenStackWrapper
import me.nn2.nn2openstackplugin.support.MessageHelper
import org.apache.logging.log4j.LogManager
import org.opensearch.rest.RestChannel
import org.opensearch.rest.RestRequest
import java.util.*

class NetworkProcessor() : IProcessor {
    private val logger = LogManager.getLogger(NetworkProcessor::class.java)

    @Throws
    override fun process(wrapper: OpenStackWrapper, channel: RestChannel, request: RestRequest?) {
        val networking = wrapper.networking()
        try {
            var dto: Set<Any> = setOf()
            with(request?.path()?.lowercase(Locale.getDefault())!!) {
                when {
                    contains("networks") -> dto = networking.getNetworks().toSet()
                    contains("subnets") -> dto = networking.getSubnets().toSet()
                    contains("ports") -> dto = networking.getPorts().toSet()
                    contains("routers") -> dto = networking.getRouters().toSet()
                    contains("securityGroups") -> dto = networking.getSecurityGroups().toSet()
                    contains("securityRules") -> dto = networking.getSecurityGroupRules().toSet()
                    contains("quotas") -> dto = networking.getQuotas().toSet()
                    contains("floatingIps") -> dto = networking.getFloatingIps().toSet()
                }
            }
            MessageHelper.sendResponse(channel, dto)
        } catch (e: Exception) {
            logger.error(e.message, e)
            MessageHelper.sendExceptionMessage(channel, e)
        }
    }
}