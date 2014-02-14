package org.easyHttp

import io.netty.handler.codec.http.HttpRequest
import io.netty.handler.codec.http.multipart.HttpPostRequestEncoder
import io.netty.handler.codec.http.DefaultFullHttpRequest
import io.netty.handler.codec.http.HttpVersion
import io.netty.handler.codec.http.HttpMethod

class FormStreamer : Streamer("application/x-www-form-urlencoded", "multipart/form-data") {
    override fun streamData(data: Any?, method: HttpMethod, rawPath: String): HttpRequest {
        val request = DefaultFullHttpRequest(HttpVersion.HTTP_1_1, method, rawPath)
        val encoder = HttpPostRequestEncoder(request, false)
        val nameValues = data as Map<String, *>
        for ((name, value) in nameValues) {
            encoder.addBodyAttribute(name, value.toString())
        }
        return encoder.finalizeRequest()!!
    }


}

