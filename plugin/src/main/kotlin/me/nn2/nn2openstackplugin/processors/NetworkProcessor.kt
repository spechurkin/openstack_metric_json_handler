package me.nn2.nn2openstackplugin.processors

import me.nn2.libs.OpenStackWrapper
import me.nn2.nn2openstackplugin.support.MessageHelper
import org.apache.logging.log4j.LogManager
import org.opensearch.rest.RestChannel

class NetworkProcessor() : IProcessor {
    private val logger = LogManager.getLogger(NetworkProcessor::class.java)

    @Throws
    override fun process(metric: String, wrapper: OpenStackWrapper, channel: RestChannel) {
        val wrapper = wrapper.networking()
        try {
            var dto: Set<Any> = setOf()
            when (metric) {
                "networks" -> dto = wrapper.getNetworks().toSet()
                "subnets" -> dto = wrapper.getSubnets().toSet()
                "ports" -> dto = wrapper.getPorts().toSet()
                "routers" -> dto = wrapper.getRouters().toSet()
                "securityGroups" -> dto = wrapper.getSecurityGroups().toSet()
                "securityRules" -> dto = wrapper.getSecurityGroupRules().toSet()
                "quotas" -> dto = wrapper.getQuotas().toSet()
                "floatingIps" -> dto = wrapper.getFloatingIps().toSet()
            }
            MessageHelper.sendResponse(channel, dto)
        } catch (e: Exception) {
            logger.error(e.message, e)
            MessageHelper.sendExceptionMessage(channel, e)
        }
    }
}