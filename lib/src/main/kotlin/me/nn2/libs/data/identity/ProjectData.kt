package me.nn2.libs.data.identity


import com.google.gson.annotations.SerializedName

data class ProjectsResponse(
    val projects: List<ProjectData>
)

data class ProjectData(
    val id: String,
    val name: String,
    val description: String,
    @SerializedName("domain_id")
    val domainId: String,
    @SerializedName("is_domain")
    val isDomain: Boolean,
    val enabled: Boolean,
    val options: Map<String, Any>? = emptyMap(),
    @SerializedName("parent_id")
    val parentId: String,
    val tags: List<String>
)