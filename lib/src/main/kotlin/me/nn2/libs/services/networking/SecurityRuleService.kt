package me.nn2.libs.services.networking

import me.nn2.libs.data.networking.SecurityRuleData
import me.nn2.libs.services.IMetricService
import org.openstack4j.api.OSClient
import org.openstack4j.model.network.SecurityGroupRule

class SecurityRuleService(override val client: OSClient.OSClientV3) : IMetricService {

    fun getSecurityRules(): List<SecurityRuleData> {
        return convertToDto()
    }

    fun convertToDto(router: SecurityGroupRule): SecurityRuleData {
        return SecurityRuleData(
            id = router.id,
            securityGroup = client.networking().securitygroup().get(router.securityGroupId).name,
            description = router.description,
            protocol = router.protocol,
            remoteIpPrefix = router.remoteIpPrefix,
            direction = router.direction,
            etherType = router.etherType,
            portRangeMin = router.portRangeMin,
            portRangeMax = router.portRangeMax
        )
    }

    private fun convertToDto(): List<SecurityRuleData> {
        return client.networking().securityrule().list().map {
            convertToDto(it)
        }
    }
}