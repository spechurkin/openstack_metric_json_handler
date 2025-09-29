package me.nn2.libs.data.compute

import java.util.*

data class HostAggregateData(
    val id: String,
    val name: String,
    val hosts: List<String>,
    val availabilityZone: String,
    val createdAt: Date,
    val updatedAt: Date,
    val idDeleted: Boolean
)