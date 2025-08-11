import java.net.HttpURLConnection
import java.net.URL

class IdentityExtractionService {
    companion object {
        private fun getToken(
            keystoneUrl: String, username: String, password: String
        ): String {
            val authJson = """
        {
          "auth": {
            "identity": {
              "methods": ["password"],
              "password": {
                "user": {
                  "name": "$username",
                  "domain": { "name": "default" },
                  "password": "$password"
                }
              }
            },
            "scope": {
              "project": {
                "name": "admin",
                "domain": { "name": "default" }
              }
            }
          }
        }
    """.trimIndent()

            val url = URL("$keystoneUrl/auth/tokens")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.setRequestProperty("Content-Type", "application/json")
            connection.doOutput = true

            connection.outputStream.use { os ->
                os.write(authJson.toByteArray(Charsets.UTF_8))
            }

            val token = connection.getHeaderField("X-Subject-Token")
                ?: throw RuntimeException("Failed to get token from Keystone")

            return token
        }

        private fun getRequest(url: URL, token: String): String {
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.setRequestProperty("X-Auth-Token", token)
            connection.setRequestProperty("Content-Type", "application/json")

            return connection.inputStream.bufferedReader().use { it.readText() }
        }

        fun getUsers(keystoneUrl: String, username: String, password: String): String {
            val token = getToken(keystoneUrl, username, password)
            val url = URL("$keystoneUrl/users")

            return getRequest(url, token)
        }

        fun getGroups(keystoneUrl: String, username: String, password: String): String {
            val token = getToken(keystoneUrl, username, password)
            val url = URL("$keystoneUrl/groups")

            return getRequest(url, token)
        }

        fun getProjects(keystoneUrl: String, username: String, password: String): String {
            val token = getToken(keystoneUrl, username, password)
            val url = URL("$keystoneUrl/projects")

            return getRequest(url, token)
        }
    }
}