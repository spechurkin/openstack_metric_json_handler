package me.nn2.libs.data.storage

import org.openstack4j.model.storage.block.Volume
import java.util.*

data class VolumeData(
    val id: String,
    val name: String?,
    val description: String?,
    val size: Int,
    val status: Volume.Status?,
    val metadata: List<String>?,
    var created: Date?,
)

data class VolumeTypeData(
    val id: String,
    val name: String,
    val specs: Map<String?, String?>?
)