package me.nn2.nn2openstackplugin.processors

import me.nn2.libs.OpenStackWrapper
import me.nn2.nn2openstackplugin.support.MessageHelper
import org.apache.logging.log4j.LogManager
import org.opensearch.rest.RestChannel

class NetworkProcessor() : IProcessor {
    private val logger = LogManager.getLogger(NetworkProcessor::class.java)

    @Throws
    override fun process(metric: String, wrapper: OpenStackWrapper, channel: RestChannel) {
        val networking = wrapper.networking()
        try {
            var dto: Set<Any> = setOf()
            when (metric) {
                "networks" -> dto = networking.getNetworks().toSet()
                "subnets" -> dto = networking.getSubnets().toSet()
                "ports" -> dto = networking.getPorts().toSet()
                "routers" -> dto = networking.getRouters().toSet()
                "securityGroups" -> dto = networking.getSecurityGroups().toSet()
                "securityRules" -> dto = networking.getSecurityGroupRules().toSet()
                "quotas" -> dto = networking.getQuotas().toSet()
                "floatingIps" -> dto = networking.getFloatingIps().toSet()
            }
            MessageHelper.sendResponse(channel, dto)
        } catch (e: Exception) {
            logger.error(e.message, e)
            MessageHelper.sendExceptionMessage(channel, e)
        }
    }
}