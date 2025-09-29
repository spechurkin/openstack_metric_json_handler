package me.nn2.libs.data

import java.util.*

data class ServiceData(
    val id: String,
    val status: String,
    val state: String,
    val host: String,
    val zone: String,
    val reasonDisabled: String,
    val updatedAt: Date
)