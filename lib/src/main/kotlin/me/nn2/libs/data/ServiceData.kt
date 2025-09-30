package me.nn2.libs.data

import java.util.*

data class ServiceData(
    val binary: String,
    val status: String,
    val state: String,
    val host: String,
    val zone: String,
    val updatedAt: Date
)