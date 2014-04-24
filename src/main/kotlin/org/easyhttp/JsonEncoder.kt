package org.easyHttp

import io.netty.handler.codec.http.HttpRequest
import io.netty.handler.codec.http.multipart.HttpPostRequestEncoder
import com.google.gson.Gson


public class JsonEncoder : ContentEncoder("application/json", "application/json;charset=\\w*\\W*\\w*") {
    override fun encode(data: Any?): Any {
        if (data is String) {
            return data
        }
        val jsonConverter = Gson()
        return jsonConverter.toJson(data)!!
    }
}