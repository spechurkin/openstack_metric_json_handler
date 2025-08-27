import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import services.wrappers.OpenStackWrapper
import java.io.File

val identityUrl = "https://10.0.2.15:5000/v3/"
val login = "admin"
val password = "59da528341503a01d9f1df2e5fcb5d76f8c07e64ab791c59937a93"

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
    val wrapper = OpenStackWrapper(identityUrl, login, password, "default", "admin")

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