package me.nn2.libs.services

import me.nn2.libs.data.identity.DomainData
import me.nn2.libs.data.identity.GroupData
import me.nn2.libs.data.identity.ProjectData
import me.nn2.libs.data.identity.UserData
import org.openstack4j.api.OSClient
import org.openstack4j.model.identity.v3.Domain
import org.openstack4j.model.identity.v3.Group
import org.openstack4j.model.identity.v3.Project
import org.openstack4j.model.identity.v3.User

class IdentityService(override val client: OSClient.OSClientV3) : IMetricService {

    fun getUsers(): List<UserData> {
        return client.identity().users().list().map {
            convertUserToDto(it)
        }
    }

    fun getGroups(): List<GroupData> {
        return client.identity().groups().list().map {
            convertGroupToDto(it)
        }
    }

    fun getProjects(): List<ProjectData> {
        return client.identity().projects().list().map {
            convertProjectToDto(it)
        }
    }

    fun getDomains(): List<DomainData> {
        return client.identity().domains().list().map {
            convertDomainToDto(it)
        }
    }

    fun getProjectIdByName(projectName: String): String? {
        return client.identity().projects().list().find { it.name == projectName }?.id
    }

    private fun convertUserToDto(user: User): UserData {
        return UserData(
            id = user.id,
            name = user.name,
            description = user.description,
            email = user.email,
            domainId = user.domainId,
            enabled = user.isEnabled,
            defaultProjectId = user.defaultProjectId
        )
    }

    private fun convertGroupToDto(group: Group): GroupData {
        return GroupData(
            id = group.id,
            name = group.name,
            description = group.description,
            domainId = group.domainId,
            groupUsers = client.identity()
                .groups().listGroupUsers(group.id)
                .map { user -> user.name }
        )
    }

    fun convertProjectToDto(project: Project): ProjectData {
        return ProjectData(
            id = project.id,
            name = project.name,
            description = project.description,
            domainId = project.domainId,
            enabled = project.isEnabled,
            parentId = project.parentId,
            parents = project.parents
        )
    }

    private fun convertDomainToDto(domain: Domain): DomainData {
        return DomainData(
            id = domain.id,
            name = domain.name,
            description = domain.description,
            enabled = domain.isEnabled
        )
    }
}