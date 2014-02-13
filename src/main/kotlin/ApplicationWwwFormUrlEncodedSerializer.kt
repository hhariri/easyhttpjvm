package org.easyHttp

import io.netty.handler.codec.http.multipart.HttpPostRequestEncoder
import io.netty.handler.codec.http.HttpRequest
import jet.Map
import java.beans.Introspector
import java.util.ArrayList
import java.lang.reflect.Method


public class ApplicationWwwFormUrlEncodedSerializer: ContentSerializer("application/x-www-form-urlencoded", "multipart/form-data") {
    override fun serialize(request: HttpRequest, data: Any?): HttpRequest {
        val encoder = HttpPostRequestEncoder(request, false)
        var postData : Map<String, *>? = null
        when (data) {
            is Map<*,*> -> { postData = data as Map<String, *> }
            else -> { if (data != null) { postData = convertClassToKeyValue(data) }}
        }
        for ((name, value) in postData) {
            encoder.addBodyAttribute(name, value.toString())
        }
        return encoder.finalizeRequest()!!
    }
}




