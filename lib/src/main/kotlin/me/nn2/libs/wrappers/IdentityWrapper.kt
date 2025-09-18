package me.nn2.libs.wrappers

import me.nn2.libs.data.identity.DomainData
import me.nn2.libs.data.identity.GroupData
import me.nn2.libs.data.identity.ProjectData
import me.nn2.libs.data.identity.UserData
import org.openstack4j.api.OSClient.OSClientV3
import org.openstack4j.model.identity.v3.Domain
import org.openstack4j.model.identity.v3.User

class IdentityWrapper(client: OSClientV3) : AWrapper(client) {
    fun getUsers(): List<UserData> {
        return client.identity().users().list().map {
            convertUserToDto(it)
        }
    }

    fun getGroups(): List<GroupData> {
        return client.identity().groups().list().map { group ->
            GroupData(
                id = group.id,
                name = group.name,
                description = group.description,
                domainId = group.domainId,
                groupUsers = client.identity()
                    .groups().listGroupUsers(group.id)
                    .map { it.name }
            )
        }
    }

    // TODO: Отслеживать обновления openstack4j, на данный момент вывод projects выдаёт ошибку
    fun getProjects(): List<ProjectData> {
        /*        return client.identity().projects().list().map {
                    ProjectData(
                        id = it.id,
                        name = it.name,
                        description = it.description,
                        domainId = it.domainId,
                        domain = convertDomainToDto(it.domain),
                        enabled = it.isEnabled,
                        parentId = it.parentId,
                        parents = it.parents
                    )
                }*/
        return listOf(
            ProjectData(
                id = "it.id",
                name = "it.name",
                description = "it.description",
                domainId = "it.domainId",
                domain = DomainData(
                    id = "domain.id",
                    name = "domain.name",
                    description = "domain.description",
                    enabled = false
                ),
                enabled = false,
                parentId = "it.parentId",
                parents = "it.parents"
            )
        )
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
            domainId = user.domainId,
            enabled = user.isEnabled,
            defaultProjectId = user.defaultProjectId
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
