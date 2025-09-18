package me.nn2.libs.data.identity


import com.google.gson.annotations.SerializedName

data class GroupsResponse(
    val groups: List<GroupData>
)

data class GroupData(
    val id: String,
    val name: String,
    val description: String,
    @SerializedName("domain_id")
    val domainId: String,
)