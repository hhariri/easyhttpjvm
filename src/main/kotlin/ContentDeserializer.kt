package org.easyHttp

import java.util.HashMap

abstract public class ContentDeserializer(vararg val mediaTypes: String) {
    open fun canDeserialize(mediaType: String): Boolean {
        for (supportedMediaType in mediaTypes) {
            if (mediaType.matches(supportedMediaType)) {
                return true
            }
        }
        return false
    }
    abstract fun deserialize<T>(input: String, objectType: Class<T>): T
}