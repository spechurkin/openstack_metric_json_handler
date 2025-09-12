package me.nn2.libs.data.compute

import org.openstack4j.model.compute.Image.Status
import java.util.*

data class ImageData(
    var id: String?,
    var name: String?,
    var minRam: Int?,
    var minDisk: Int?,
    var size: Long?,
    var status: Status?,
    var created: Date?,
    var updated: Date?,
)