package my.proj.work.services

import com.google.gson.GsonBuilder
import org.openstack4j.api.OSClient.OSClientV3
import java.io.File

interface IMetricService {
    val client: OSClientV3

    fun toJson(list: List<Any>, metric: String?): String? {
        val gson = GsonBuilder().serializeNulls().setPrettyPrinting().create()
        File("src/main/resources/metric/$metric.json").parentFile?.mkdirs()
        File("src/main/resources/metric/$metric.json").writeText(gson.toJson(list))
        return gson.toJson(list)
    }
}