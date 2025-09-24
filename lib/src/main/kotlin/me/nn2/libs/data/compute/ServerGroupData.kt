package me.nn2.libs.data.compute

data class ServerGroupData(
    val id: String,
    val name: String,
    val members: List<String?>,
    val policy: String
)