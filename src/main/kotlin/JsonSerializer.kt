package org.easyHttp

import io.netty.handler.codec.http.HttpRequest
import io.netty.handler.codec.http.multipart.HttpPostRequestEncoder
import com.google.gson.Gson


public class JsonSerializer: ContentSerializer("application/json", "application/json;charset=\\w*\\W*\\w*") {
    override fun serialize(request: HttpRequest, data: Any?): HttpRequest {
        val jsonConverter = Gson()

        val json = jsonConverter.toJson(data)

        return encoder.finalizeRequest()!!
    }
}