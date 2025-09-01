package org.opensearch.openstackMetric.config

import services.wrappers.OpenStackWrapper
import java.util.concurrent.atomic.AtomicReference

class OpenStackManager() {
    private val cfgRef = AtomicReference<OpenStackConfig?>()
    private var wrapperRef: OpenStackWrapper? = null

    fun setConfig(cfg: OpenStackConfig) {
        cfgRef.set(cfg)
        // Создаём новый wrapper (при необходимости — с флагом allowInsecure в своём конструкторе)
        wrapperRef = OpenStackWrapper(
            authUrl = cfg.authUrl,
            username = cfg.username,
            password = cfg.password,
            domain = cfg.domain,
            project = cfg.project
        )
    }

    fun getConfig(): OpenStackConfig? = cfgRef.get()

    fun wrapper(): OpenStackWrapper =
        wrapperRef ?: error("OpenStack config is not set. POST /nn2/openstack/config first.")
}