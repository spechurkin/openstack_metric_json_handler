package me.nn2.libs.data.identity

data class ProjectData(
    val id: String?,
    val name: String?,
    val description: String?,
    val domainId: String?,
    val domain: DomainData?,
    val enabled: Boolean?,
    val parentId: String?,
    val parents: String?,
)