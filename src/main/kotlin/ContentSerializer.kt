package org.easyHttp

import jet.Map
import io.netty.handler.codec.http.HttpRequest


abstract public class ContentSerializer(vararg val mediaTypes: String) {
    open fun canSerialize(mediaType: String): Boolean {
        for (supportedMediaType in mediaTypes) {
            if (mediaType.matches(supportedMediaType)) {
                return true
            }
        }
        return false
    }
    abstract fun serialize(request: HttpRequest, data: Any?): HttpRequest
}