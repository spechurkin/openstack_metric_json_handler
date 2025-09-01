package org.opensearch.openstackMetricHandler.config

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.opensearch.core.common.bytes.BytesArray
import org.opensearch.core.rest.RestStatus
import org.opensearch.rest.BytesRestResponse

internal fun okJson(json: String) = BytesRestResponse(
    RestStatus.OK,
    "application/json",
    BytesArray(json)
)

internal fun err(status: RestStatus, msg: String) = BytesRestResponse(
    status,
    "application/json",
    BytesArray(
        """
        "{"error":"${msg.replace("\"", "\\\"")}}"}
        """
    )
)

internal val gson: Gson = GsonBuilder().serializeNulls().setPrettyPrinting().create()

data class OpenStackConfig(
    val authUrl: String,
    val username: String,
    val password: String,
    val domain: String = "Default",
    val project: String = "admin",
    val allowInsecure: Boolean = false
)