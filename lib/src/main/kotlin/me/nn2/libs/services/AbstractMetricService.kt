package me.nn2.libs.services

import org.openstack4j.api.OSClient.OSClientV3

abstract class AbstractMetricService(open val client: OSClientV3) {
    fun addSymbolsToCopy(name: String?): String {
        return "$name-${getRandomString()}"
    }

    private fun getRandomString(): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..5)
            .map { allowedChars.random() }
            .joinToString("")
    }
}