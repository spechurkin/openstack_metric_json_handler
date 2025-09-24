package me.nn2.libs.services.networking

import me.nn2.libs.data.networking.SecurityGroupData
import me.nn2.libs.services.IMetricService
import org.openstack4j.api.OSClient

class SecurityGroupService(override val client: OSClient.OSClientV3) : IMetricService {
    fun getSecurityGroups(): List<SecurityGroupData> {
        return convertToDto()
    }

    fun getSecurityGroupIdByName(securityGroup: String): String {
        return client.networking().securitygroup().list().find { it.name == securityGroup }!!.id
    }

    private fun convertToDto(): List<SecurityGroupData> {
        return client.networking().securitygroup().list().map {
            SecurityGroupData(
                id = it.id,
                name = it.name,
                description = it.description,
                rules = it.rules.map { rule ->
                    rule.id
                }
            )
        }
    }
}