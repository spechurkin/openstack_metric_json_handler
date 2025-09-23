package me.nn2.nn2openstackplugin.processors

import me.nn2.libs.OpenStackWrapper
import me.nn2.nn2openstackplugin.support.MessageHelper
import org.apache.logging.log4j.LogManager
import org.opensearch.rest.RestChannel
import org.opensearch.rest.RestRequest
import java.util.*

class IdentityProcessor() : IProcessor {
    private val logger = LogManager.getLogger(IdentityProcessor::class.java)

    @Throws
    override fun process(wrapper: OpenStackWrapper, channel: RestChannel, request: RestRequest?) {
        val identity = wrapper.identity()
        try {
            var dto: Set<Any> = setOf()
            with(request?.path()?.lowercase(Locale.getDefault())!!) {
                when {
                    contains("users") -> dto = identity.getUsers().toSet()
                    contains("groups") -> dto = identity.getGroups().toSet()
                    contains("projects") -> dto = identity.getProjects().toSet()
                    contains("domains") -> dto = identity.getDomains().toSet()
                    else -> logger.warn("Bad request!")
                }
            }
            MessageHelper.sendResponse(channel, dto)
        } catch (e: Exception) {
            logger.error(e.message, e)
            MessageHelper.sendExceptionMessage(channel, e)
        }
    }
}
