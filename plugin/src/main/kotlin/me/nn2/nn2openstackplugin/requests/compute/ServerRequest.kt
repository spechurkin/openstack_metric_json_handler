package me.nn2.nn2openstackplugin.requests.compute

class ServerRequest(
    val serverName: String,
    val imageName: String,
    val flavorName: String,
    val adminPass: String,
    val keyPair: String?,
    val networkNames: List<String>
) {
    class Builder {
        private var serverName: String = ""
        private var imageName: String = ""
        private var flavorName: String = ""
        private var adminPass: String = "admin"
        private var keyPair: String? = null
        private var networkNames: List<String> = emptyList()

        fun serverName(serverName: String) = apply { this.serverName = serverName }
        fun imageName(imageName: String) = apply { this.imageName = imageName }
        fun flavorName(flavorName: String) = apply { this.flavorName = flavorName }
        fun adminPass(adminPass: String) = apply { this.adminPass = adminPass }
        fun keyPair(keyPair: String?) = apply { this.keyPair = keyPair }
        fun networkNames(networkNames: List<String>) = apply { this.networkNames = networkNames }

        fun build(): ServerRequest {
            require(serverName.isNotBlank()) { "serverName is required" }
            require(imageName.isNotBlank()) { "imageName is required" }
            require(flavorName.isNotBlank()) { "flavorName is required" }
            return ServerRequest(serverName, imageName, flavorName, adminPass, keyPair, networkNames)
        }
    }
}