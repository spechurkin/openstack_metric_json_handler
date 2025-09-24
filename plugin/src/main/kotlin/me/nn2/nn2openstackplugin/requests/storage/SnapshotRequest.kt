package me.nn2.nn2openstackplugin.requests.storage

class SnapshotRequest(val snapshotName: String?, val description: String?, val volumeName: String) {
    class Builder {
        private var snapshotName: String? = ""
        private var description: String? = ""
        private var volumeName: String = ""

        fun snapshotName(snapshotName: String) = apply { this.snapshotName = snapshotName }
        fun description(description: String?) = apply { this.description = description }
        fun volumeName(volumeName: String) = apply { this.volumeName = volumeName }

        fun build(): SnapshotRequest {
            require(volumeName.isNotBlank()) { "volumeName must not be null" }
            return SnapshotRequest(snapshotName, description, volumeName)
        }
    }
}