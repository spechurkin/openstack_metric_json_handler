package me.nn2.libs.data.storage

import java.util.Date

data class SnapshotData(
    val id: String,
    val name: String?,
    val description: String?,
    val volumeId: String,
    val status: String,
    val size: Int,
    val createdAt: Date,
)
