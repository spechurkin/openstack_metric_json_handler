package me.nn2.nn2openstackplugin.requests.storage

class SnapshotUpdateRequest(val snapshotName: String, val description: String?, val newName: String?) {
    class Builder {
        private var snapshotName: String = ""
        private var description: String? = ""
        private var newName: String? = ""

        fun snapshotName(snapshotName: String) = apply { this.snapshotName = snapshotName }
        fun description(description: String?) = apply { this.description = description }
        fun newName(newName: String) = apply { this.newName = newName }

        fun build(): SnapshotUpdateRequest {
            require(snapshotName.isNotBlank()) { "snapshotName must not be null" }
            return SnapshotUpdateRequest(snapshotName, description, newName)
        }
    }
}