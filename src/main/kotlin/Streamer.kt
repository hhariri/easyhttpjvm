package org.easyHttp

import io.netty.handler.codec.http.HttpRequest
import io.netty.handler.codec.http.HttpMethod


abstract class Streamer(vararg val mediaTypes: String) {
    open fun canStream(mediaType: String): Boolean {
        for (supportedMediaType in mediaTypes) {
            if (mediaType.matches(supportedMediaType)) {
                return true
            }
        }
        return false
    }

    abstract fun streamData(data: Any?, method: HttpMethod, rawPath: String): HttpRequest
}