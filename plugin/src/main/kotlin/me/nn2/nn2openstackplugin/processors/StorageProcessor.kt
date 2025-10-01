package me.nn2.nn2openstackplugin.processors

import me.nn2.libs.OpenStackWrapper
import me.nn2.nn2openstackplugin.requests.storage.*
import me.nn2.nn2openstackplugin.support.MessageHelper
import org.apache.logging.log4j.LogManager
import org.opensearch.rest.RestChannel
import org.opensearch.rest.RestRequest

class StorageProcessor() : IProcessor {
    private val logger = LogManager.getLogger(StorageProcessor::class.java)

    @Throws
    override fun process(wrapper: OpenStackWrapper, channel: RestChannel, request: RestRequest?) {
        val storage = wrapper.blockStorage()
        var dto: Set<Any> = setOf()

        when (request?.method()) {
            RestRequest.Method.GET -> with(request.path()) {
                try {
                    when {
                        contains("volumes") -> dto = storage.getVolumes().toSet()
                        contains("volumeTypes") -> dto = storage.getVolumeTypes().toSet()
                        contains("backups") -> dto = storage.getBackups().toSet()
                        contains("snapshots") -> dto = storage.getSnapshots().toSet()
                        contains("services") -> dto = storage.getServices().toSet()
                        else -> logger.warn("Bad request!")
                    }
                    MessageHelper.sendResponse(channel, dto)
                } catch (e: Exception) {
                    logger.error(e.message, e)
                    MessageHelper.sendExceptionMessage(channel, e)
                }
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

                    contains("volumeTypes/create") -> {
                        try {
                            val body: Map<String, Any> = request.contentParser().map()

                            val volumeTypeRequest = VolumeTypeRequest.Builder()
                                .typeName(body["typeName"].toString())
                                .apply {
                                    body["specs"]?.let {
                                        @Suppress("UNCHECKED_CAST")
                                        specs(it as Map<String, Any>?)
                                    }
                                }.build()

                            logger.warn(volumeTypeRequest.specs.toString())
                            logger.warn(volumeTypeRequest.specs.toString())
                            logger.warn(volumeTypeRequest.specs.toString())

                            storage.createVolumeType(
                                typeName = volumeTypeRequest.typeName,
                                specs = volumeTypeRequest.specs
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

                    contains("backups/create") -> {
                        try {
                            val body: Map<String, Any> = request.contentParser().map()

                            val backupRequest = BackupRequest.Builder()
                                .backupName(body["backupName"].toString())
                                .volumeName(body["volumeName"].toString())
                                .apply {
                                    body["description"]?.let { description(it.toString()) }
                                }
                                .build()

                            storage.createBackup(
                                backupName = backupRequest.backupName,
                                description = backupRequest.description,
                                volumeName = backupRequest.volumeName
                            )

                        } catch (e: Exception) {
                            logger.error(e.message, e)
                            MessageHelper.sendExceptionMessage(channel, e)
                        }
                    }

                    else -> logger.warn("Bad request!")
                }
            }

            RestRequest.Method.PUT -> with(request.path()) {
                when {
                    contains("volumes/update") -> {
                        try {
                            val body: Map<String, Any> = request.contentParser().map()

                            val volumeUpdateRequest = VolumeUpdateRequest.Builder()
                                .volumeName(body["volumeName"].toString())
                                .apply {
                                    body["newName"]?.let { newName(it.toString()) }
                                    body["description"]?.let { description(it.toString()) }
                                    body["newSize"]?.let { newSize(it as Int) }
                                }
                                .build()

                            storage.updateVolume(
                                volumeUpdateRequest.volumeName,
                                volumeUpdateRequest.newName,
                                volumeUpdateRequest.description,
                                volumeUpdateRequest.newSize
                            )
                        } catch (e: Exception) {
                            logger.error(e.message, e)
                            MessageHelper.sendExceptionMessage(channel, e)
                        }
                    }

                    contains("snapshots/update") -> {
                        try {
                            val body: Map<String, Any> = request.contentParser().map()

                            val snapshotUpdateRequest = SnapshotUpdateRequest.Builder()
                                .snapshotName(body["snapshotName"].toString())
                                .apply {
                                    body["newName"]?.let { newName(it.toString()) }
                                    body["description"]?.let { description(it.toString()) }
                                }.build()

                            storage.updateSnapshot(
                                snapshotUpdateRequest.snapshotName,
                                snapshotUpdateRequest.newName,
                                snapshotUpdateRequest.description
                            )
                        } catch (e: Exception) {
                            logger.error(e.message, e)
                            MessageHelper.sendExceptionMessage(channel, e)
                        }
                    }

                    contains("backups/restore") -> {
                        try {
                            val body: Map<String, Any> = request.contentParser().map()

                            val backupRestoreRequest = BackupRestoreRequest.Builder()
                                .backupName(body["snapshotName"].toString())
                                .volumeName(body["volumeName"].toString())
                                .apply {
                                    body["newName"]?.let { newName(it.toString()) }
                                }
                                .build()

                            storage.restoreBackup(
                                backupRestoreRequest.backupName,
                                backupRestoreRequest.newName,
                                backupRestoreRequest.volumeName
                            )
                        } catch (e: Exception) {
                            logger.error(e.message, e)
                            MessageHelper.sendExceptionMessage(channel, e)
                        }
                    }

                    else -> logger.warn("Bad request!")
                }
            }

            RestRequest.Method.DELETE -> with(request.path()) {
                when {
                    contains("volumes/delete") -> {
                        storage.deleteVolume(request.param("volumeName"))
                    }

                    contains("volumeTypes/delete") -> {
                        storage.deleteVolumeType(request.param("typeName"))
                    }

                    contains("snapshots/delete") -> {
                        storage.deleteSnapshot(request.param("snapshotName"))
                    }

                    contains("backups/delete") -> {
                        storage.deleteBackup(request.param("backupName"))
                    }

                    else -> logger.warn("Bad request!")
                }
            }

            RestRequest.Method.OPTIONS -> TODO()
            RestRequest.Method.HEAD -> TODO()
            RestRequest.Method.PATCH -> TODO()
            RestRequest.Method.TRACE -> TODO()
            RestRequest.Method.CONNECT -> TODO()
            null -> TODO()
        }
    }
}
