package me.nn2.libs.data.compute

import java.util.*

data class MigrationData(
    val id: String,
    val status: String,
    val sourceNode: String,
    val destNode: String,
    val sourceCompute: String,
    val destCompute: String,
    val destHost: String,
    val createdAt: Date,
    val updatedAt: Date
)