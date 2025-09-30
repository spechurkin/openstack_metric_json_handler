package me.nn2.libs.wrappers

import me.nn2.libs.data.identity.DomainData
import me.nn2.libs.data.identity.GroupData
import me.nn2.libs.data.identity.ProjectData
import me.nn2.libs.data.identity.UserData
import me.nn2.libs.services.IdentityService
import org.openstack4j.api.OSClient.OSClientV3

class IdentityWrapper(client: OSClientV3) : AbstractWrapper(client) {
    fun getUsers(): List<UserData> {
        return IdentityService(client).getUsers()
    }

    fun getGroups(): List<GroupData> {
        return IdentityService(client).getGroups()
    }

    fun getProjects(): List<ProjectData> {
        return IdentityService(client).getProjects()
    }

    fun getDomains(): List<DomainData> {
        return IdentityService(client).getDomains()
    }
}
