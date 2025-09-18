package me.nn2.nn2openstackplugin.processors

import me.nn2.libs.OpenStackWrapper
import me.nn2.nn2openstackplugin.support.MessageHelper
import org.apache.logging.log4j.LogManager
import org.opensearch.rest.RestChannel

class ComputeProcessor() : IProcessor {
    private val logger = LogManager.getLogger(ComputeProcessor::class.java)

    @Throws
    override fun process(metric: String, wrapper: OpenStackWrapper, channel: RestChannel) {
        val wrapper = wrapper.compute()
        try {
            var dto: Set<Any> = setOf()
            when (metric) {
                "servers" -> dto = wrapper.getServers().toSet()
                "images" -> dto = wrapper.getImages().toSet()
                "flavors" -> dto = wrapper.getFlavors().toSet()
                "keypairs" -> dto = wrapper.getKeypairs().toSet()
                "services" -> dto = wrapper.getServices().toSet()
                "floatingIps" -> dto = wrapper.getFloatingIps().toSet()
                "hosts" -> dto = wrapper.getHosts().toSet()
                "zones" -> dto = wrapper.getZones().toSet()
                "migrations" -> dto = wrapper.getMigrations().toSet()
                "hypervisors" -> dto = wrapper.getHypervisors().toSet()
                "hostAggregates" -> dto = wrapper.getHostAggregates().toSet()
                "serverGroups" -> dto = wrapper.getServerGroups().toSet()
                "securityGroups" -> dto = wrapper.getSecurityGroups().toSet()
                "securityRules" -> dto = wrapper.getSecurityRules().toSet()
            }
            MessageHelper.sendResponse(channel, dto)
        } catch (e: Exception) {
            logger.error(e.message, e)
            MessageHelper.sendExceptionMessage(channel, e)
        }
    }
}