package proj.work.consoleCreation.network

import org.openstack4j.api.Builders
import proj.work.consoleCreation.compute.os

fun main() {
}

fun createSecGroup(secGroupName: String = "DefaultSecGroup", description: String = "") {
    val secGroupCreate = Builders.securityGroup()
        .name(secGroupName)
        .description(description)
        .build()

    os.networking().securitygroup().create(secGroupCreate)
}

fun updateSecGroup(secGroupId: String, secGroupName: String = "DefaultSecGroup", description: String = "") {
    os.networking().securitygroup().update(
        secGroupId,
        Builders.securityGroupUpdate()
            .name(secGroupName)
            .description(description)
            .build()
    )
}

fun deleteSecGroup(secGroupId: String) {
    os.networking().securitygroup().delete(secGroupId)
}

fun createSecRule(
    secGroupId: String,
    portMin: Int,
    portMax: Int = portMin,
    etherType: String = "IPv4",
    direction: String = "ingress"
) {
    os.networking().securityrule().create(
        Builders.securityGroupRule()
            .securityGroupId(secGroupId)
            .protocol("tcp")
            .portRangeMin(portMin)
            .portRangeMax(portMax)
            .ethertype(etherType)
            .direction(direction)
            .build()
    )
}

fun deleteRule(secRuleId: String) {
    os.networking().securityrule().delete(secRuleId)
}

fun getSecGroupIdByName(secGroupName: String): String? {
    return os.networking().securitygroup().list(mapOf("name" to secGroupName)).firstOrNull()?.id
}