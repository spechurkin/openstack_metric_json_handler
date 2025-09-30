package me.nn2.libs.data.networking

data class SecurityRuleData(
    val id: String?,
    val tenantId: String,
    val securityGroup: String,
    val description: String?,
    val protocol: String?,
    val remoteIpPrefix: String?,
    val direction: String?,
    val etherType: String?,
    val portRangeMin: Int?,
    val portRangeMax: Int?,
)