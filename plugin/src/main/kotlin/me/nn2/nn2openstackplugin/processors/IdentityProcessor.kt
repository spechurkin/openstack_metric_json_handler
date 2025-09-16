package me.nn2.nn2openstackplugin.processors

import me.nn2.libs.OpenStackWrapper
import me.nn2.nn2openstackplugin.support.MessageHelper
import org.opensearch.rest.RestChannel
import org.slf4j.LoggerFactory

class IdentityProcessor() : IProcessor {
    private val logger = LoggerFactory.getLogger(ComputeProcessor::class.java)

    @Throws
    override fun process(metric: String, wrapper: OpenStackWrapper, channel: RestChannel) {
        val wrapper = wrapper.identity()
        try {
            var dto = ""
            when (metric) {
                "users" -> dto = wrapper.getUsers()
                "groups" -> dto = wrapper.getGroups()
                "projects" -> dto = wrapper.getProjects()
                "domains" -> dto = wrapper.getDomains()
            }
            MessageHelper.sendMessage(channel, dto)
        } catch (e: Exception) {
            logger.error(e.message)
            MessageHelper.sendExceptionMessage(channel, e)
        }
    }
}
