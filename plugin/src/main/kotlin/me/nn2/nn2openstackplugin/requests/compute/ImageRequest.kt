package me.nn2.nn2openstackplugin.requests.compute

class ImageRequest(
    val imageName: String,
    val minRamGb: Int,
    val minDiskGb: Int,
    val visibility: String,
    val urlToImg: String
) {
    class Builder {
        private var imageName: String = ""
        private var minRamGb: Int = 0
        private var minDiskGb: Int = 0
        private var visibility: String = "Общий"
        private var urlToImg: String = ""

        fun imageName(imageName: String) = apply { this.imageName = imageName }
        fun minRamGb(minRamGb: Int) = apply { this.minRamGb = minRamGb }
        fun minDiskGb(minDiskGb: Int) = apply { this.minDiskGb = minDiskGb }
        fun visibility(visibility: String) = apply { this.visibility = visibility }
        fun urlToImg(urlToImg: String) = apply { this.urlToImg = urlToImg }

        fun build(): ImageRequest {
            require(imageName.isNotBlank()) { "serverName is required" }
            require(visibility.isNotBlank()) { "visibility is required" }
            require(urlToImg.isNotBlank()) { "url ot IMG is required" }
            return ImageRequest(imageName, minRamGb, minDiskGb, visibility, urlToImg)
        }
    }
}