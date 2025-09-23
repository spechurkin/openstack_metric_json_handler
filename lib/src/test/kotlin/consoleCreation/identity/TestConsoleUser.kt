package proj.work.consoleCreation.identity

import org.openstack4j.api.Builders
import org.openstack4j.model.identity.v3.User
import proj.work.consoleCreation.compute.os

fun main() {

}

fun createUser(
    username: String,
    description: String = "",
    email: String = "",
    password: String,
    domainId: String = "default",
    projectId: String,
    enabled: Boolean = true
) {
    val userCreate = Builders.user()
        .name(username)
        .description(description)
        .email(email)
        .password(password)
        .domainId(domainId)
        .defaultProjectId(projectId)
        .enabled(enabled)
        .build()

    os.identity().users().create(userCreate)
}

fun updateUser(
    userId: String,
    username: String,
    description: String = "",
    email: String = "",
    domainId: String = "default"
) {
    val user: User = os.identity().users().get(userId)
    os.identity().users().update(
        user.toBuilder()
            .name(username)
            .description(description)
            .email(email)
            .defaultProjectId(domainId)
            .build()
    )
}

fun enableDisableUser(userId: String, enabled: Boolean) {
    val user: User = os.identity().users().get(userId)
    os.identity().users().update(
        user.toBuilder()
            .enabled(enabled)
            .build()
    )
}

fun removeUser(userId: String?) {
    os.identity().users().delete(userId)
}

fun getUserIdByName(username: String): String? {
    return os.identity().users().list().find { it.name == username }?.id
}