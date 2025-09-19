package me.nn2.libs.data.compute

import org.openstack4j.model.image.v2.Image.ImageStatus
import java.util.*

data class ImageData(
    var id: String?,
    var name: String?,
    var minRam: Long?,
    var minDisk: Long?,
    var size: Long?,
    var status: ImageStatus?,
    var createdAt: Date?,
    var updatedAt: Date?,
)