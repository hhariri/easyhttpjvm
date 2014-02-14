package org.easyHttp

import jet.Map
import io.netty.handler.codec.http.HttpRequest


abstract public class ContentEncoder(vararg val mediaTypes: String) {
    open fun canEncode(mediaType: String): Boolean {
        for (supportedMediaType in mediaTypes) {
            if (mediaType.matches(supportedMediaType)) {
                return true
            }
        }
        return false
    }
    abstract fun encode(data: Any?): Any
}