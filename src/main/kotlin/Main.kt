package my.proj.work

import com.google.gson.GsonBuilder
import my.proj.work.services.compute.FlavorService
import my.proj.work.services.compute.ImageService
import my.proj.work.services.compute.ServerService
import org.openstack4j.api.OSClient.OSClientV3
import org.openstack4j.model.common.Identifier
import org.openstack4j.openstack.OSFactory
import java.io.File

val os: OSClientV3 = OSFactory.builderV3().endpoint("https://10.0.2.15:5000/v3")
    .credentials("e6a47c690cf84969b594d33000fbbda3", "4bea85208709a9b37328c276cd82f509977")
    .authenticate()

fun toJson(list: List<Any>, metric: String?) {
    val gson = GsonBuilder().serializeNulls().setPrettyPrinting().create()
    File("src/main/resources/metric/$metric.json").parentFile?.mkdirs()
    File("src/main/resources/metric/$metric.json").writeText(gson.toJson(list))
}

fun main() {
    ImageService(os).toJson()
    toJson(os.compute().host().list(), "compute/host")
    toJson(os.compute().zones().list(), "compute/zones")
    FlavorService(os).toJson()
    ServerService(os).toJson()
    toJson(os.compute().floatingIps().list(), "compute/floatingIpsq")
    toJson(os.compute().hostAggregates().list(), "compute/hostAggregates")
    toJson(os.compute().hypervisors().list(), "compute/hypervisors")
    toJson(os.compute().keypairs().list(), "compute/keypairs")
    toJson(os.compute().migrations().list(), "compute/migrations")
    toJson(os.compute().securityGroups().list(), "compute/securityGroups")
    toJson(os.compute().serverGroups().list(), "compute/serverGroups")
    toJson(os.compute().services().list(), "compute/services")

    toJson(os.blockStorage().volumes().list(), "blockStorage/volumes")
    toJson(os.blockStorage().backups().list(), "blockStorage/backups")
    toJson(os.blockStorage().snapshots().list(), "blockStorage/snapshots")
    toJson(os.blockStorage().services().list(), "blockStorage/services")

    toJson(os.networking().network().list(), "networking/network")
    toJson(os.networking().subnet().list(), "networking/subnet")
    toJson(os.networking().port().list(), "networking/port")
    toJson(os.networking().router().list(), "networking/router")
    toJson(os.networking().securitygroup().list(), "networking/securitygroup")
    toJson(os.networking().quotas().get(), "networking/quotas")
    toJson(os.networking().floatingip().list(), "networking/floatingip")
}