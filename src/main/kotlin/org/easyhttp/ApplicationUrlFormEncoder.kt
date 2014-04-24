package org.easyHttp

import io.netty.handler.codec.http.multipart.HttpPostRequestEncoder
import io.netty.handler.codec.http.HttpRequest
import kotlin.Map
import java.beans.Introspector
import java.util.ArrayList
import java.lang.reflect.Method


public class ApplicationUrlFormEncoder : ContentEncoder("application/x-www-form-urlencoded", "multipart/form-data") {
    override fun encode(data: Any?): Any {
        var postData : Map<String, *>? = null
        when (data) {
            is Map<*,*> -> { postData = data as Map<String, *> }
            else -> { if (data != null) { postData = convertClassToKeyValue(data) }}
        }
        return postData!!
    }
}






