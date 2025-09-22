package me.nn2.nn2openstackplugin.processors

import me.nn2.libs.OpenStackWrapper
import me.nn2.nn2openstackplugin.support.MessageHelper
import org.apache.logging.log4j.LogManager
import org.opensearch.rest.RestChannel

class ComputeProcessor() : IProcessor {
    private val logger = LogManager.getLogger(ComputeProcessor::class.java)

    @Throws
    override fun process(metric: String, wrapper: OpenStackWrapper, channel: RestChannel) {
        val compute = wrapper.compute()
        try {
            var dto: Set<Any> = setOf()
            when (metric) {
                "servers" -> dto = compute.getServers().toSet()
                "images" -> dto = compute.getImages().toSet()
                "flavors" -> dto = compute.getFlavors().toSet()
                "keypairs" -> dto = compute.getKeypairs().toSet()
                "services" -> dto = compute.getServices().toSet()
                "floatingIps" -> dto = compute.getFloatingIps().toSet()
                "hosts" -> dto = compute.getHosts().toSet()
                "zones" -> dto = compute.getZones().toSet()
                "migrations" -> dto = compute.getMigrations().toSet()
                "hypervisors" -> dto = compute.getHypervisors().toSet()
                "hostAggregates" -> dto = compute.getHostAggregates().toSet()
                "serverGroups" -> dto = compute.getServerGroups().toSet()
                "securityGroups" -> dto = compute.getSecurityGroups().toSet()
                "securityRules" -> dto = compute.getSecurityRules().toSet()
            }
            MessageHelper.sendResponse(channel, dto)
        } catch (e: Exception) {
            logger.error(e.message, e)
            MessageHelper.sendExceptionMessage(channel, e)
        }
    }
}