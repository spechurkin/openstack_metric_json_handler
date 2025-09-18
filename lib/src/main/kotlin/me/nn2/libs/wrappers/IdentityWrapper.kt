package me.nn2.libs.wrappers

import com.google.gson.Gson
import me.nn2.libs.data.identity.*
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class IdentityWrapper(
    private val keystoneUrl: String,
    private val username: String,
    private val password: String,
    private val project: String,
    private val domain: String,
    private val insecure: Boolean = true,
) {
    private val httpClient = httpClient()

    fun httpClient(): HttpClient {
        return if (insecure) {
            val trustAllCerts = arrayOf<TrustManager>(
                object : X509TrustManager {
                    override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
                    override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}
                    override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
                }
            )

            val sslContext = SSLContext.getInstance("TLS").apply {
                init(null, trustAllCerts, SecureRandom())
            }

            HttpClient.newBuilder()
                .sslContext(sslContext)
                .build()
        } else {
            HttpClient.newBuilder().build()
        }
    }

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

    fun getUsers(): List<UserData> {
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
        val users = Gson().fromJson(response.body(), UsersResponse::class.java).users

        return users
    }

    fun getGroups(): List<GroupData> {
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
        val groups = Gson().fromJson(response.body(), GroupsResponse::class.java).groups

        return groups
    }

    fun getProjects(): List<ProjectData> {
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
        val projects = Gson().fromJson(response.body(), ProjectsResponse::class.java).projects

        return projects
    }

    fun getDomains(): List<DomainData> {
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
        val domains = Gson().fromJson(response.body(), DomainsResponse::class.java).domains

        return domains
    }
}
