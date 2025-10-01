package me.nn2.nn2openstackplugin.requests.storage

class VolumeTypeRequest(
    val typeName: String,
    val specs: Map<String, String>?
) {
    class Builder {
        private var typeName: String = ""
        private var specs: Map<String, String>? = null

        fun typeName(typeName: String) = apply { this.typeName = typeName }
        fun specs(specs: Map<String, Any>?) = apply { this.specs = specs?.mapValues { it.value.toString() } }

        fun build(): VolumeTypeRequest {
            return VolumeTypeRequest(typeName, specs)
        }
    }
}