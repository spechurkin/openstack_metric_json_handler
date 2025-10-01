package me.nn2.nn2openstackplugin.requests.storage

class VolumeUpdateRequest(
    val volumeName: String,
    val newName: String?,
    val newSize: Int?,
    val description: String?
) {
    class Builder {
        private var volumeName: String = ""
        private var newName: String? = null
        private var newSize: Int? = null
        private var description: String? = null

        fun volumeName(volumeName: String) = apply { this.volumeName = volumeName }
        fun newName(newName: String?) = apply { this.newName = newName }
        fun newSize(newSize: Int) = apply { this.newSize = newSize }
        fun description(description: String) = apply { this.description = description }

        fun build(): VolumeUpdateRequest {
            require(volumeName.isNotBlank()) { "volumeName must not be null" }

            return VolumeUpdateRequest(volumeName, newName, newSize, description)
        }
    }
}