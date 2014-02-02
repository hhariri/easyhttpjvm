package org.easyHttp

import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.codec.http.HttpObject
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.http.HttpHeaders
import io.netty.handler.codec.http.HttpContent
import io.netty.util.CharsetUtil
import io.netty.handler.codec.http.LastHttpContent
import io.netty.handler.codec.http.HttpResponse


class HttpClientHandler(private val callback: (String) -> Unit): SimpleChannelInboundHandler<HttpObject>() {
    override fun channelRead0(ctx: ChannelHandlerContext?, msg: HttpObject?) {
        if (msg is HttpResponse) {
            val response = (msg as HttpResponse)

            println("STATUS: " + response.getStatus())
            println("VERSION: " + response.getProtocolVersion())
            println()


            if (HttpHeaders.isTransferEncodingChunked(response)) {
                println("CHUNKED CONTENT {")
            } else {
                println("CONTENT {")
            }
        }
        if (msg is HttpContent) {
            val content = (msg as HttpContent)

            val message = content.content()!!.toString(CharsetUtil.UTF_8)!!
            if (content is LastHttpContent) {
                println("} END OF CONTENT")
            }
            callback(message)
        }
    }

}