package me.nn2.nn2openstackplugin.support

import me.nn2.libs.support.ResponseHelper
import org.opensearch.core.rest.RestStatus
import org.opensearch.rest.BytesRestResponse
import org.opensearch.rest.RestChannel

class MessageHelper {
    companion object {
        fun sendExceptionMessage(
            channel: RestChannel,
            e: Exception,
        ) {
            return channel.sendResponse(
                BytesRestResponse(
                    RestStatus.OK,
                    "application/json; charset=UTF-8",
                    ResponseHelper.processExceptionResponse(e),
                ),
            )
        }

        fun sendMessage(
            channel: RestChannel,
            message: String
        ) {
            return channel.sendResponse(
                BytesRestResponse(
                    RestStatus.OK,
                    "application/json; charset=UTF-8",
                    ResponseHelper.processMessageResponse(message),
                )
            )
        }

        fun <T> sendResponse(
            channel: RestChannel,
            items: Set<T>
        ) {
            return channel.sendResponse(
                BytesRestResponse(
                    RestStatus.OK,
                    "application/json; charset=UTF-8",
                    ResponseHelper.processResponse(items),
                )
            )
        }

    }
}