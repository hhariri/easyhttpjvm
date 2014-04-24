package org.easyHttp

import java.util.HashMap

abstract public class ContentDecoder(vararg val mediaTypes: String) {
    open fun canDecode(mediaType: String): Boolean {
        for (supportedMediaType in mediaTypes) {
            if (mediaType.matches(supportedMediaType)) {
                return true
            }
        }
        return false
    }
    abstract fun decode<T>(input: String, objectType: Class<T>): T
}