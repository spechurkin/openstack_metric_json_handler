package me.nn2.nn2openstackplugin.requests.compute

class FlavorRequest(
    val flavorName: String,
    val ramMb: Int,
    val diskGb: Int,
    val ephemeralGb: Int,
    val swapMb: Int,
    val vcpus: Int,
    val rxtxFactor: Float,
    val isPublic: Boolean,
) {
    class Builder {
        private var flavorName: String = ""
        private var ramMb: Int = 256
        private var diskGb: Int = 1
        private var ephemeralGb: Int = 0
        private var swapMb: Int = 0
        private var vcpus: Int = 1
        private var rxtxFactor: Float = 1F
        private var isPublic: Boolean = true

        fun flavorName(flavorName: String) = apply { this.flavorName = flavorName }
        fun ramMb(ramMb: Int) = apply { this.ramMb = ramMb }
        fun diskGb(diskGb: Int) = apply { this.diskGb = diskGb }
        fun ephemeralGb(ephemeralGb: Int) = apply { this.ephemeralGb = ephemeralGb }
        fun swapMb(swapMb: Int) = apply { this.swapMb = swapMb }
        fun vcpus(vcpus: Int) = apply { this.vcpus = vcpus }
        fun rxtxFactor(rxtxFactor: Int) = apply { this.rxtxFactor = rxtxFactor.toFloat() }
        fun isPublic(isPublic: Boolean) = apply { this.isPublic = isPublic }

        fun build(): FlavorRequest {
            require(flavorName.isNotBlank()) { "flavorName is required" }
            return FlavorRequest(flavorName, ramMb, diskGb, ephemeralGb, swapMb, vcpus, rxtxFactor, isPublic)
        }
    }
}