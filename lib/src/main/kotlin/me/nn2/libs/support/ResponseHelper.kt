package me.nn2.libs.support

import com.google.gson.GsonBuilder
import java.util.*

class ResponseHelper {
    companion object {
        fun processExceptionResponse(e: Exception): String {
            val source: MutableMap<String, String> = mutableMapOf()
            source["exceptionMessage"] = Objects.toString(e.message)
            val gson = GsonBuilder().serializeNulls().disableHtmlEscaping()
                .create()
            return gson.toJson(source)
        }

        fun processMessageResponse(message: String): String {
            val source: MutableMap<String, String> = mutableMapOf()
            source["message"] = message
            val gson = GsonBuilder().serializeNulls().disableHtmlEscaping().create()
            return gson.toJson(source)
        }

        fun <T> processResponse(items: T): String {
            val gson = GsonBuilder().serializeNulls().disableHtmlEscaping().create()
            return gson.toJson(items)
        }
    }
}