package me.nn2.nn2openstackplugin.processors

import me.nn2.libs.OpenStackWrapper
import me.nn2.nn2openstackplugin.support.MessageHelper
import org.apache.logging.log4j.LogManager
import org.opensearch.rest.RestChannel

class StorageProcessor() : IProcessor {
    private val logger = LogManager.getLogger(StorageProcessor::class.java)

    @Throws
    override fun process(metric: String, wrapper: OpenStackWrapper, channel: RestChannel) {
        val wrapper = wrapper.blockStorage()
        try {
            var dto: Set<Any> = setOf()
            when (metric) {
                "volumes" -> dto = wrapper.getVolumes().toSet()
                "backups" -> dto = wrapper.getBackups().toSet()
                "snapshots" -> dto = wrapper.getSnapshots().toSet()
                "services" -> dto = wrapper.getServices().toSet()
            }
            MessageHelper.sendResponse(channel, dto)
        } catch (e: Exception) {
            logger.error(e.message)
            MessageHelper.sendExceptionMessage(channel, e)
        }
    }
}
