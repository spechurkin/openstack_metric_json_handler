package me.nn2.libs.services.networking

import me.nn2.libs.data.networking.SecurityRuleData
import me.nn2.libs.services.AbstractMetricService
import me.nn2.libs.services.IdentityService
import org.openstack4j.api.Builders
import org.openstack4j.api.OSClient.OSClientV3
import org.openstack4j.model.network.SecurityGroupRule

class SecurityRuleService(client: OSClientV3) : AbstractMetricService(client) {
    fun getSecurityRules(): List<SecurityRuleData> {
        return convertToDto()
    }

    fun createSecurityGroupRule(
        projectName: String,
        groupName: String,
        description: String,
        protocol: String,
        remoteIpPrefix: String,
        direction: String,
        etherType: String,
        portRangeMin: Int,
        portRangeMax: Int
    ) {
        val securityGroupBuilder = Builders.securityGroupRule()
            .tenantId(IdentityService(client).getProject(projectName)!!.id)
            .securityGroupId(SecurityGroupService(client).getSecurityGroup(groupName)!!.id)
            .description(description)
            .protocol(protocol)
            .remoteIpPrefix(remoteIpPrefix)
            .direction(direction)
            .ethertype(etherType)
            .portRangeMin(portRangeMin)
            .portRangeMax(portRangeMax)

        client.networking().securityrule().create(securityGroupBuilder.build())
    }

    fun deleteSecurityGroupRule(ruleId: String) {
        client.networking().securityrule().delete(ruleId)
    }

    fun convertToDto(securityGroupRule: SecurityGroupRule): SecurityRuleData {
        return SecurityRuleData(
            id = securityGroupRule.id,
            tenantId = securityGroupRule.tenantId,
            securityGroup = client.networking().securitygroup().get(securityGroupRule.securityGroupId).name,
            description = securityGroupRule.description,
            protocol = securityGroupRule.protocol,
            remoteIpPrefix = securityGroupRule.remoteIpPrefix,
            direction = securityGroupRule.direction,
            etherType = securityGroupRule.etherType,
            portRangeMin = securityGroupRule.portRangeMin,
            portRangeMax = securityGroupRule.portRangeMax
        )
    }

    private fun convertToDto(): List<SecurityRuleData> {
        return client.networking().securityrule().list().map {
            convertToDto(it)
        }
    }
}