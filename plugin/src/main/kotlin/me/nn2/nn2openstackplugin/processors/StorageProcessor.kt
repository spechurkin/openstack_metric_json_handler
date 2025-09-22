package me.nn2.nn2openstackplugin.processors

import me.nn2.libs.OpenStackWrapper
import me.nn2.nn2openstackplugin.support.MessageHelper
import org.apache.logging.log4j.LogManager
import org.opensearch.rest.RestChannel

class StorageProcessor() : IProcessor {
    private val logger = LogManager.getLogger(StorageProcessor::class.java)

    @Throws
    override fun process(metric: String, wrapper: OpenStackWrapper, channel: RestChannel) {
        val storage = wrapper.blockStorage()
        try {
            var dto: Set<Any> = setOf()
            when (metric) {
                "volumes" -> dto = storage.getVolumes().toSet()
                "backups" -> dto = storage.getBackups().toSet()
                "snapshots" -> dto = storage.getSnapshots().toSet()
                "services" -> dto = storage.getServices().toSet()
            }
            MessageHelper.sendResponse(channel, dto)
        } catch (e: Exception) {
            logger.error(e.message, e)
            MessageHelper.sendExceptionMessage(channel, e)
        }
    }
}
