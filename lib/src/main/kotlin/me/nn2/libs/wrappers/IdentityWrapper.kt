package me.nn2.libs.wrappers

import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class IdentityWrapper(
    private val keystoneUrl: String,
    private val username: String,
    private val password: String,
    private val project: String,
    private val domain: String,
) {
    private val httpClient = HttpClient.newBuilder().build()

    private fun getToken(): String {
        val authJson =
            """
            {
              "auth": {
                "identity": {
                  "methods": ["password"],
                  "password": {
                    "user": {
                      "name": "$username",
                      "domain": { "name": "$domain" },
                      "password": "$password"
                    }
                  }
                },
                "scope": {
                  "project": {
                    "name": "$project",
                    "domain": { "name": "$domain" }
                  }
                }
              }
            }
            """.trimIndent()

        val request =
            HttpRequest.newBuilder()
                .uri(URI.create("$keystoneUrl/auth/tokens"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(authJson))
                .build()

        val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())
        if (response.statusCode() != 201) {
            throw RuntimeException("Failed to authentificate: ${response.statusCode()} ${response.body()}")
        }

        return response.headers().firstValue("X-Subject-Token")
            .orElseThrow { RuntimeException("Missing X-Subject-Token") }
    }

    fun getUsers(): String {
        val token = getToken()
        val request =
            HttpRequest.newBuilder()
                .uri(URI.create("$keystoneUrl/users"))
                .header("X-Auth-Token", token)
                .GET()
                .build()

        val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())
        if (response.statusCode() !in 200..209) {
            throw RuntimeException("Failed to get users: ${response.statusCode()} ${response.body()}")
        }

        return response.body()
    }

    fun getGroups(): String {
        val token = getToken()
        val request =
            HttpRequest.newBuilder()
                .uri(URI.create("$keystoneUrl/groups"))
                .header("X-Auth-Token", token)
                .GET()
                .build()

        val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())
        if (response.statusCode() !in 200..209) {
            throw RuntimeException("Failed to get groups: ${response.statusCode()} ${response.body()}")
        }

        return response.body()
    }

    fun getProjects(): String {
        val token = getToken()
        val request =
            HttpRequest.newBuilder()
                .uri(URI.create("$keystoneUrl/projects"))
                .header("X-Auth-Token", token)
                .GET()
                .build()

        val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())
        if (response.statusCode() !in 200..209) {
            throw RuntimeException("Failed to get projects: ${response.statusCode()} ${response.body()}")
        }

        return response.body()
    }

    fun getDomains(): String {
        val token = getToken()
        val request =
            HttpRequest.newBuilder()
                .uri(URI.create("$keystoneUrl/domains"))
                .header("X-Auth-Token", token)
                .GET()
                .build()

        val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())
        if (response.statusCode() !in 200..209) {
            throw RuntimeException("Failed to get projects: ${response.statusCode()} ${response.body()}")
        }

        return response.body()
    }
}
