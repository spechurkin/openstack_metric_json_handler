package me.nn2.libs.data.compute

import java.util.*

data class KeypairData(
    val id: Int?,
    val name: String?,
    val fingerprint: String?,
    val user: String?,
    val publicKey: String?,
    val privateKey: String?,
    val createdAt: Date?,
    val updatedAt: Date?,
    val isDeleted: Boolean?,
    val deletedAt: Date?
)