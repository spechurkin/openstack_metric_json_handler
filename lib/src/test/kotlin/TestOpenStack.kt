package proj.work

import me.nn2.libs.OpenStackWrapper

const val identityUrl = "https://10.1.0.48:5000/v3"
const val login = "admin"
const val password = "0d96e9f5d0ea5b2f8dab4"
const val domain = "Default"
const val project = "admin"

fun main() {
    val wrapper = OpenStackWrapper(identityUrl, login, password, domain, project, true)

//    println(wrapper.compute().getServers())
//    println(wrapper.compute().getImages())
//    println(wrapper.compute().getFlavors())
//    println(wrapper.compute().getHostAggregates())
//    println(wrapper.compute().getHypervisors())
//    println(wrapper.compute().getKeypairs())
//    println(wrapper.compute().getMigrations())
//    println(wrapper.compute().getZones())
//    println(wrapper.compute().getServices())
//    println(wrapper.compute().getServerGroups())
//    println(wrapper.client().blockStorage().volumes().listVolumeTypes())
//
//    println(wrapper.identity().getUsers())
//    println(wrapper.identity().getGroups())
    println(wrapper.identity().getProjects())
//    println(wrapper.identity().getDomains())
//
//    println(wrapper.blockStorage().getVolumes())
//    println(wrapper.blockStorage().getBackups())
//    println(wrapper.blockStorage().getSnapshots())
//    println(wrapper.blockStorage().getServices())
//
//    println(wrapper.networking().getNetworks())
//    println(wrapper.networking().getSubnets())
//    println(wrapper.networking().getRouters())
//    println(wrapper.networking().getPorts())
//    println(wrapper.networking().getFloatingIps())
//    println(wrapper.networking().getSecurityGroups())
//    println(wrapper.networking().getSecurityGroupRules())
}