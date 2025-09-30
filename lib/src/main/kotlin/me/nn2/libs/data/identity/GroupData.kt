package me.nn2.libs.data.identity

data class GroupData(
    val id: String?,
    val name: String?,
    val description: String? = null,
    val domain: String?,
    val groupUsers: List<String?>
)