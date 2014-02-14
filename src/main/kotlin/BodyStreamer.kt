package org.easyHttp

import io.netty.handler.codec.http.HttpRequest
import io.netty.handler.codec.http.HttpMethod
import io.netty.handler.codec.http.DefaultFullHttpRequest
import io.netty.handler.codec.http.HttpVersion
import io.netty.handler.codec.http.multipart.HttpPostRequestEncoder
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import io.netty.util.CharsetUtil

public class BodyStreamer : Streamer("application/json", "application/json;charset=\\w*\\W*\\w*") {
    override fun streamData(data: Any?, method: HttpMethod, rawPath: String): HttpRequest {
        val s = data.toString()
        val buffer = Unpooled.copiedBuffer(s, CharsetUtil.UTF_8)
        return DefaultFullHttpRequest(HttpVersion.HTTP_1_1, method, rawPath, buffer)
    }

}