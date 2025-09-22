package me.nn2.libs.data.compute

data class FlavorData(
    var id: String?,
    var name: String?,
    var ram: Int?,
    var vcpu: Int?,
    var disk: Int?,
    var ephemeral: Int?,
    var swap: Int?,
    val rxtx: Float,
)