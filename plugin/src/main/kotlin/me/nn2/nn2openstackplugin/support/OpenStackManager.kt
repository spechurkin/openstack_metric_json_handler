package me.nn2.nn2openstackplugin.support

import me.nn2.libs.OpenStackWrapper
import me.nn2.nn2openstackplugin.support.settings.OpenStackConfig
import java.util.concurrent.atomic.AtomicReference

class OpenStackManager() {
    private val cfgRef = AtomicReference<OpenStackConfig?>()
    private var wrapperRef: OpenStackWrapper? = null

    fun setConfig(cfg: OpenStackConfig) {
        cfgRef.set(cfg)

        wrapperRef = OpenStackWrapper(
            authUrl = cfg.authUrl,
            username = cfg.username,
            password = cfg.password,
            domain = cfg.domain,
            project = cfg.project,
            allowInsecure = cfg.allowInsecure
        )
    }

    fun getConfig(): OpenStackConfig? = cfgRef.get()

    fun wrapper(): OpenStackWrapper =
        wrapperRef ?: error("OpenStack config is not set. POST /nn2/openstack/config first.")
}