package me.nn2.nn2openstackplugin.processors

import me.nn2.libs.OpenStackWrapper
import me.nn2.nn2openstackplugin.support.MessageHelper
import org.apache.logging.log4j.LogManager
import org.opensearch.rest.RestChannel
import org.opensearch.rest.RestRequest
import java.util.*

class StorageProcessor() : IProcessor {
    private val logger = LogManager.getLogger(StorageProcessor::class.java)

    @Throws
    override fun process(wrapper: OpenStackWrapper, channel: RestChannel, request: RestRequest?) {
        val storage = wrapper.blockStorage()
        try {
            var dto: Set<Any> = setOf()
            with(request?.path()?.lowercase(Locale.getDefault())!!) {
                when {
                    contains("volumes") -> dto = storage.getVolumes().toSet()
                    contains("backups") -> dto = storage.getBackups().toSet()
                    contains("snapshots") -> dto = storage.getSnapshots().toSet()
                    contains("services") -> dto = storage.getServices().toSet()
                }
            }
            MessageHelper.sendResponse(channel, dto)
        } catch (e: Exception) {
            logger.error(e.message, e)
            MessageHelper.sendExceptionMessage(channel, e)
        }
    }
}
