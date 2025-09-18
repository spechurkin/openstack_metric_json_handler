package me.nn2.libs.data.identity

import com.google.gson.annotations.SerializedName

data class UsersResponse(
    val users: List<UserData>
)

data class UserData(
    val id: String,
    val name: String,
    @SerializedName("domain_id")
    val domainId: String,
    val enabled: Boolean,
    @SerializedName("default_project_id")
    val defaultProjectId: String? = null,
    @SerializedName("password_expires_at")
    val passwordExpiresAt: String? = null,
    val email: String? = null,
    val options: Map<String, Any>? = emptyMap()
)