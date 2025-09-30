package me.nn2.libs.services

import me.nn2.libs.data.identity.DomainData
import me.nn2.libs.data.identity.GroupData
import me.nn2.libs.data.identity.ProjectData
import me.nn2.libs.data.identity.UserData
import org.openstack4j.api.OSClient.OSClientV3
import org.openstack4j.model.identity.v3.Domain
import org.openstack4j.model.identity.v3.Group
import org.openstack4j.model.identity.v3.Project
import org.openstack4j.model.identity.v3.User

class IdentityService(client: OSClientV3) : AbstractMetricService(client) {
    private val domainService = client.identity().domains()

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

    fun getProject(projectName: String): Project? {
        return client.identity().projects().list().firstOrNull { it.name == projectName }
    }

    fun getDomains(): List<DomainData> {
        return client.identity().domains().list().map {
            convertDomainToDto(it)
        }
    }

    private fun convertUserToDto(user: User): UserData {
        return UserData(
            id = user.id,
            name = user.name,
            description = user.description,
            email = user.email,
            domain = domainService.get(user.domainId).name,
            enabled = user.isEnabled,
            defaultProjectName = client.identity().projects().get(user.defaultProjectId).name
        )
    }

    private fun convertGroupToDto(group: Group): GroupData {
        return GroupData(
            id = group.id,
            name = group.name,
            description = group.description,
            domain = domainService.get(group.domainId).name,
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
            domain = domainService.get(project.domainId).name,
            enabled = project.isEnabled,
            parentId = project.parentId
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