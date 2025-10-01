package me.nn2.nn2openstackplugin.requests.storage

class BackupRequest(val backupName: String, val description: String?, val volumeName: String) {
    class Builder {
        private var backupName: String = ""
        private var description: String? = ""
        private var volumeName: String = ""

        fun backupName(backupName: String) = apply { this.backupName = backupName }
        fun description(description: String?) = apply { this.description = description }
        fun volumeName(volumeName: String) = apply { this.volumeName = volumeName }

        fun build(): BackupRequest {
            require(backupName.isNotBlank()) { "backupName must not be null" }
            require(volumeName.isNotBlank()) { "volumeName must not be null" }

            return BackupRequest(backupName, description, volumeName)
        }
    }
}
