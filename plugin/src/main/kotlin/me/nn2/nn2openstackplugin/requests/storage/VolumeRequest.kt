package me.nn2.nn2openstackplugin.requests.storage

class VolumeRequest(
    val volumeName: String,
    val description: String?,
    val sizeGb: Int,
    val volumeType: String,
    val imageRef: String?
) {
    class Builder {
        private var volumeName: String = ""
        private var description: String? = null
        private var sizeGb: Int = 1
        private var volumeType: String = "__DEFAULT__"
        private var bootable: Boolean = false
        private var imageRef: String? = ""

        fun volumeName(volumeName: String) = apply { this.volumeName = volumeName }
        fun description(description: String?) = apply { this.description = description }
        fun size(sizeGb: Int) = apply { this.sizeGb = sizeGb }
        fun volumeType(volumeType: String) = apply { this.volumeType = volumeType }
        fun bootable(bootable: Boolean) = apply { this.bootable = bootable }
        fun imageRef(imageRef: String) = apply { this.imageRef = imageRef }

        fun build(): VolumeRequest {
            require(sizeGb > 0) { "size must be greater than 0" }
            if (bootable) {
                require(imageRef!!.isNotBlank()) { "imageRef must not be null" }
            }
            return VolumeRequest(volumeName, description, sizeGb, volumeType, imageRef)
        }
    }
}