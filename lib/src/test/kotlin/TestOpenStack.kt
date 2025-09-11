package proj.work

import services.wrappers.OpenStackWrapper

const val identityUrl = "https://10.0.2.15:5000/v3/"
const val login = "admin"
const val password = "59da528341503a01d9f1df2e5fcb5d76f8c07e64ab791c59937a93"
const val domain = "default"
const val project = "admin"

fun main() {
    val wrapper = OpenStackWrapper(identityUrl, login, password, domain, project)

    println(wrapper.compute().getServers())
    println(wrapper.compute().getImages())
    println(wrapper.compute().getFlavors())
    println(wrapper.compute().getHosts())
    println(wrapper.compute().getFloatingIps())
    println(wrapper.compute().getHostAggregates())
    println(wrapper.compute().getHypervisors())
    println(wrapper.compute().getKeypairs())
    println(wrapper.compute().getMigrations())
    println(wrapper.compute().getZones())
    println(wrapper.compute().getServices())
    println(wrapper.compute().getServerGroups())
    println(wrapper.compute().getSecurityGroups())
    println(wrapper.compute().getSecurityRules())

    println(wrapper.identity().getUsers())
    println(wrapper.identity().getGroups())
    println(wrapper.identity().getProjects())
    println(wrapper.identity().getDomains())

    println(wrapper.blockStorage().getVolumes())
    println(wrapper.blockStorage().getBackups())
    println(wrapper.blockStorage().getSnapshots())
    println(wrapper.blockStorage().getServices())

    println(wrapper.networking().getNetworks())
    println(wrapper.networking().getSubnets())
    println(wrapper.networking().getRouters())
    println(wrapper.networking().getPorts())
    println(wrapper.networking().getQuotas())
    println(wrapper.networking().getFloatingIps())
    println(wrapper.networking().getSecurityGroups())
}
