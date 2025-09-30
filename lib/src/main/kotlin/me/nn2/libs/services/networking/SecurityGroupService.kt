package me.nn2.libs.services.networking

import me.nn2.libs.data.networking.SecurityGroupData
import me.nn2.libs.services.AbstractMetricService
import me.nn2.libs.services.IdentityService
import org.openstack4j.api.Builders
import org.openstack4j.api.OSClient.OSClientV3
import org.openstack4j.model.network.SecurityGroup

class SecurityGroupService(client: OSClientV3) : AbstractMetricService(client) {
    private val identityService = IdentityService(client)

    fun getSecurityGroups(): List<SecurityGroupData> {
        return convertToDto()
    }

    fun getSecurityGroup(securityGroupName: String): SecurityGroup? {
        return client.networking().securitygroup().list().firstOrNull { it.name == securityGroupName }
    }

    fun createSecurityGroup(groupName: String, description: String, projectName: String) {
        if (getSecurityGroup(groupName) == null) {
            val securityGroupBuilder = Builders.securityGroup()
                .name(groupName)
                .description(description)
                .tenantId(identityService.getProject(projectName)!!.id)

            client.networking().securitygroup().create(securityGroupBuilder.build())
        } else {
            createSecurityGroup(addSymbolsToCopy(groupName), description, projectName)
        }
    }

    fun updateSecurityGroup(groupName: String, description: String) {
        val securityGroupBuilder = Builders.securityGroupUpdate()
            .name(groupName)
            .description(description)

        client.networking().securitygroup()
            .update(
                getSecurityGroup(groupName)!!.id,
                securityGroupBuilder.build()
            )
    }

    fun deleteSecurityGroup(securityGroupName: String) {
        client.networking().securitygroup().delete(getSecurityGroup(securityGroupName)!!.id)
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