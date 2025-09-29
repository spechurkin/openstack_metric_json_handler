package me.nn2.nn2openstackplugin.processors

import me.nn2.libs.OpenStackWrapper
import me.nn2.nn2openstackplugin.requests.storage.SnapshotRequest
import me.nn2.nn2openstackplugin.requests.storage.VolumeRequest
import me.nn2.nn2openstackplugin.support.MessageHelper
import org.apache.logging.log4j.LogManager
import org.opensearch.rest.RestChannel
import org.opensearch.rest.RestRequest

class StorageProcessor() : IProcessor {
    private val logger = LogManager.getLogger(StorageProcessor::class.java)

    @Throws
    override fun process(wrapper: OpenStackWrapper, channel: RestChannel, request: RestRequest?) {
        val storage = wrapper.blockStorage()

        when (request?.method()) {
            RestRequest.Method.GET ->
                try {
                    var dto: Set<Any> = setOf()
                    with(request.path()) {
                        when {
                            contains("volumes") -> dto = storage.getVolumes().toSet()
                            contains("volumeTypes") -> dto = storage.getVolumeTypes().toSet()
                            contains("backups") -> dto = storage.getBackups().toSet()
                            contains("snapshots") -> dto = storage.getSnapshots().toSet()
                            contains("services") -> dto = storage.getServices().toSet()
                            else -> logger.warn("Bad request!")
                        }
                    }
                    MessageHelper.sendResponse(channel, dto)
                } catch (e: Exception) {
                    logger.error(e.message, e)
                    MessageHelper.sendExceptionMessage(channel, e)
                }

            RestRequest.Method.POST -> with(request.path()) {
                when {
                    contains("volumes/create") -> {
                        try {
                            val body: Map<String, Any> = request.contentParser().map()

                            val volumeRequest = VolumeRequest.Builder()
                                .size(body["size"] as Int)
                                .apply {
                                    body["volumeName"]?.let { volumeName(it.toString()) }
                                    body["description"]?.let { description(it.toString()) }
                                    body["volumeType"]?.let { volumeType(it.toString()) }
                                    body["bootable"]?.let { bootable(it as Boolean) }
                                    body["imageRef"]?.let { imageRef(it.toString()) }
                                }.build()

                            storage.createVolume(
                                volumeName = volumeRequest.volumeName,
                                description = volumeRequest.description,
                                size = volumeRequest.sizeGb,
                                volumeType = volumeRequest.volumeType,
                                imageName = volumeRequest.imageRef
                            )

                        } catch (e: Exception) {
                            logger.error(e.message, e)
                            MessageHelper.sendExceptionMessage(channel, e)
                        }
                    }

                    contains("snapshots/create") -> {
                        try {
                            val body: Map<String, Any> = request.contentParser().map()

                            val snapshotRequest = SnapshotRequest.Builder()
                                .volumeName(body["volumeName"].toString())
                                .apply {
                                    body["snapshotName"]?.let { snapshotName(it.toString()) }
                                    body["description"]?.let { description(it.toString()) }
                                }
                                .build()

                            storage.createSnapshot(
                                snapshotName = snapshotRequest.snapshotName,
                                description = snapshotRequest.description,
                                volumeName = snapshotRequest.volumeName
                            )

                        } catch (e: Exception) {
                            logger.error(e.message, e)
                            MessageHelper.sendExceptionMessage(channel, e)
                        }
                    }

                    else -> logger.warn("Bad request!")
                }
            }

            RestRequest.Method.PUT -> TODO()
            RestRequest.Method.DELETE -> TODO()
            RestRequest.Method.OPTIONS -> TODO()
            RestRequest.Method.HEAD -> TODO()
            RestRequest.Method.PATCH -> TODO()
            RestRequest.Method.TRACE -> TODO()
            RestRequest.Method.CONNECT -> TODO()
            null -> TODO()
        }
    }
}
