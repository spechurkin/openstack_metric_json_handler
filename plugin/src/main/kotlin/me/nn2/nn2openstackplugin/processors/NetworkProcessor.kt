package me.nn2.nn2openstackplugin.processors

import me.nn2.libs.OpenStackWrapper
import me.nn2.nn2openstackplugin.support.MessageHelper
import org.opensearch.rest.RestChannel
import org.slf4j.LoggerFactory

class NetworkProcessor() : IProcessor {
    private val logger = LoggerFactory.getLogger(ComputeProcessor::class.java)

    @Throws
    override fun process(metric: String, wrapper: OpenStackWrapper, channel: RestChannel) {
        val wrapper = wrapper.networking()
        try {
            var dto: Set<Any> = setOf()
            when (metric) {
                "network" -> dto = wrapper.getNetworks().toSet()
                "subnet" -> dto = wrapper.getSubnets().toSet()
                "port" -> dto = wrapper.getPorts().toSet()
                "router" -> dto = wrapper.getRouters().toSet()
                "securitygroup" -> dto = wrapper.getSecurityGroups().toSet()
                "quotas" -> dto = wrapper.getQuotas().toSet()
                "floatingip" -> dto = wrapper.getFloatingIps().toSet()
            }
            MessageHelper.sendResponse(channel, dto)
        } catch (e: Exception) {
            logger.error(e.message)
            MessageHelper.sendExceptionMessage(channel, e)
        }
    }
}