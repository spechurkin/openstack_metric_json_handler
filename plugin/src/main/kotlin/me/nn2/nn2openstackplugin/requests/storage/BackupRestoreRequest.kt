package me.nn2.nn2openstackplugin.requests.storage

class BackupRestoreRequest(val backupName: String, val newName: String?, val volumeName: String?) {
    class Builder {
        private var backupName: String = ""
        private var newName: String? = ""
        private var volumeName: String = ""

        fun backupName(snapshotName: String) = apply { this.backupName = snapshotName }
        fun newName(newName: String?) = apply { this.newName = newName }
        fun volumeName(volumeName: String) = apply { this.volumeName = volumeName }

        fun build(): BackupRestoreRequest {
            require(backupName.isNotBlank()) { "snapshotName must not be null" }
            require(volumeName.isNotBlank()) { "volumeName must not be null" }
            return BackupRestoreRequest(backupName, newName, volumeName)
        }
    }
}