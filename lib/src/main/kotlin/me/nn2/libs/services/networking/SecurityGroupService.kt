package me.nn2.libs.services.networking

import me.nn2.libs.data.networking.SecurityGroupData
import me.nn2.libs.services.IMetricService
import org.openstack4j.api.OSClient

class SecurityGroupService(override val client: OSClient.OSClientV3) : IMetricService {
    fun getSecurityGroups(): List<SecurityGroupData> {
        return convertToDto()
    }

    private fun convertToDto(): List<SecurityGroupData> {
        return client.networking().securitygroup().list().map { mapEntry ->
            SecurityGroupData(
                id = mapEntry.id,
                name = mapEntry.name,
                description = mapEntry.description,
                rules = mapEntry.rules.map { rule ->
                    rule.id
                }
            )
        }
    }
}