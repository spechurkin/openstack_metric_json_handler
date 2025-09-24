package me.nn2.libs.services.compute

import me.nn2.libs.data.compute.ServerGroupData
import me.nn2.libs.services.IMetricService
import org.openstack4j.api.OSClient.OSClientV3
import org.openstack4j.model.compute.ServerGroup

class ServerGroupService(override val client: OSClientV3) : IMetricService {

    fun getServerGroup(): List<ServerGroupData> {
        return convertToDto()
    }

    private fun convertToDto(): List<ServerGroupData> {
        return client.compute().serverGroups().list().map {
            computeToDto(it)
        }
    }

    private fun computeToDto(serverGroup: ServerGroup): ServerGroupData {
        return ServerGroupData(
            id = serverGroup.id,
            name = serverGroup.name,
            members = serverGroup.members.map { client.compute().servers().get(it).name },
            policy = serverGroup.policies[0]
        )
    }
}