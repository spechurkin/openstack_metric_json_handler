package proj.work.consoleCreation.identity

import org.openstack4j.api.Builders
import proj.work.consoleCreation.compute.os

fun main() {

}

fun createUserGroup(groupName: String, description: String, domainId: String = "default", userList: List<String>) {
    val groupCreate = Builders.group()
        .name(groupName)
        .description(description)
        .domainId(domainId)
        .build()

    val group = os.identity().groups().create(groupCreate)

    userList.forEach { userId ->
        os.identity().groups().addUserToGroup(group.id, userId)
    }
}

fun addUserToGroup(groupId: String, userId: String) {
    os.identity().groups().addUserToGroup(groupId, userId)
}

fun removeUserFromGroup(groupId: String, userId: String) {
    os.identity().groups().removeUserFromGroup(groupId, userId)
}

fun updateUserGroup(groupId: String, groupName: String, description: String) {
    val group = os.identity().groups().get(groupId)
    os.identity().groups().update(
        group.toBuilder()
            .name(groupName)
            .description(description)
            .build()
    )
}

fun removeUserGroup(groupId: String) {
    os.identity().groups().delete(groupId)
}

fun gerGroupIdByName(groupName: String): String? {
    return os.identity().groups().list().find { it.name == groupName }?.id
}