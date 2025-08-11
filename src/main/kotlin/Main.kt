package my.proj.work

import IdentityExtractionService
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import org.openstack4j.api.OSClient.OSClientV3
import org.openstack4j.model.common.Identifier
import org.openstack4j.openstack.OSFactory
import java.io.File

val identityUrl = "https://10.0.2.15:5000/v3"
val login = "admin"
val password = "4bea85208709a9b37328c276cd82f509977"

val os: OSClientV3 = OSFactory.builderV3().endpoint(identityUrl)
    .credentials(login, password, Identifier.byName("default"))
    .authenticate()

fun toJson(list: List<Any>, metric: String?) {
    val gson = GsonBuilder().serializeNulls().setPrettyPrinting().create()
    File("src/main/resources/metric/$metric.json").parentFile?.mkdirs()
    File("src/main/resources/metric/$metric.json").writeText(gson.toJson(list))
}

fun toJson(jsonString: String, metric: String?) {
    val gson = GsonBuilder().serializeNulls().setPrettyPrinting().create()
    val json = JsonParser.parseString(jsonString).asJsonObject.getAsJsonArray(metric!!.split("/")[1])
    File("src/main/resources/metric/$metric.json").parentFile?.mkdirs()
    File("src/main/resources/metric/$metric.json").writeText(gson.toJson(json))
}

fun main() {
    os.useRegion("RegionOne")

    toJson(os.compute().images().list(), "compute/images")
    toJson(os.compute().host().list(), "compute/host")
    toJson(os.compute().zones().list(), "compute/zones")
    toJson(os.compute().flavors().list(), "compute/flavors")
    toJson(os.compute().servers().list(), "compute/servers")
    toJson(os.compute().floatingIps().list(), "compute/floatingIps")
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

    toJson(os.compute().securityGroups().list().map { group ->
        group.rules
    }, "compute/rules")

    toJson(
        IdentityExtractionService.getUsers(
            identityUrl,
            login,
            password
        ), "identity/users"
    )

    toJson(
        IdentityExtractionService.getGroups(
            identityUrl,
            login,
            password
        ), "identity/groups"
    )

    toJson(
        IdentityExtractionService.getProjects(
            identityUrl,
            login,
            password
        ), "identity/projects"
    )
}