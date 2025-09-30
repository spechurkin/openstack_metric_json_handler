package me.nn2.libs.services.compute

import me.nn2.libs.data.compute.ServerGroupData
import me.nn2.libs.services.AbstractMetricService
import org.openstack4j.api.OSClient.OSClientV3
import org.openstack4j.model.compute.ServerGroup

class ServerGroupService(client: OSClientV3) : AbstractMetricService(client) {
    fun getServerGroups(): List<ServerGroupData> {
        return convertToDto()
    }

    fun getServerGroup(serverGroupName: String): ServerGroup? {
        return client.compute().serverGroups().list().firstOrNull { it.name == serverGroupName }
    }

    fun createServerGroup(groupName: String, policy: String) {
        if (getServerGroup(groupName) == null) {
            client.compute().serverGroups().create(groupName, policy)
        } else {
            createServerGroup(addSymbolsToCopy(groupName), policy)
        }
    }

    fun deleteServerGroup(groupName: String) {
        client.compute().serverGroups().delete(getServerGroup(groupName)!!.id)
    }

    fun computeToDto(serverGroup: ServerGroup): ServerGroupData {
        return ServerGroupData(
            id = serverGroup.id,
            name = serverGroup.name,
            members = serverGroup.members.map { client.compute().servers().get(it).name },
            policy = serverGroup.policies[0]
        )
    }

    private fun convertToDto(): List<ServerGroupData> {
        return client.compute().serverGroups().list().map {
            computeToDto(it)
        }
    }
}