package me.nn2.libs.data.identity

data class DomainsResponse(
    val domains: List<DomainData>
)

data class DomainData(
    val id: String,
    val name: String,
    val description: String,
    val enabled: Boolean,
    val options: Map<String, Any>? = emptyMap(),
    val tags: List<Any>
)