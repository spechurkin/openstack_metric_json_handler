package me.nn2.nn2openstackplugin.processors

import me.nn2.libs.OpenStackWrapper
import me.nn2.nn2openstackplugin.support.MessageHelper
import org.apache.logging.log4j.LogManager
import org.opensearch.rest.RestChannel

class IdentityProcessor() : IProcessor {
    private val logger = LogManager.getLogger(IdentityProcessor::class.java)

    @Throws
    override fun process(metric: String, wrapper: OpenStackWrapper, channel: RestChannel) {
        val identity = wrapper.identity()
        try {
            var dto: Set<Any> = setOf()
            when (metric) {
                "users" -> dto = identity.getUsers().toSet()
                "groups" -> dto = identity.getGroups().toSet()
                "projects" -> dto = identity.getProjects().toSet()
                "domains" -> dto = identity.getDomains().toSet()
            }
            MessageHelper.sendResponse(channel, dto)
        } catch (e: Exception) {
            logger.error(e.message, e)
            MessageHelper.sendExceptionMessage(channel, e)
        }
    }
}
