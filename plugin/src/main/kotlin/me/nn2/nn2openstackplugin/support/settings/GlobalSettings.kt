package me.nn2.nn2openstackplugin.support.settings

class GlobalSettings {
    companion object {
        private const val BASE_PATH: String = "/nn2"

        const val COMPUTE_PATH: String = "$BASE_PATH/compute"
        const val IDENTITY_PATH: String = "$BASE_PATH/identity"
        const val NETWORKING_PATH: String = "$BASE_PATH/networking"
        const val STORAGE_PATH: String = "$BASE_PATH/blockStorage"
        const val CONFIG_PATH: String = "$BASE_PATH/openstack/config"
    }
}