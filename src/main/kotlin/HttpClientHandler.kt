package org.easyHttp

import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.codec.http.HttpObject
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.http.HttpHeaders
import io.netty.handler.codec.http.HttpContent
import io.netty.util.CharsetUtil
import io.netty.handler.codec.http.LastHttpContent
import io.netty.handler.codec.http.HttpResponse


class HttpClientHandler(private val callback: Response.() -> Unit): SimpleChannelInboundHandler<HttpObject>() {

    val requestResponse = Response()

    override fun channelRead0(ctx: ChannelHandlerContext?, msg: HttpObject?) {
        if (msg is HttpResponse) {
            val response = (msg as HttpResponse)
            requestResponse.statusCode = response.getStatus().toString()
        }
        if (msg is HttpContent) {
            val content = (msg as HttpContent)
            val message = content.content()!!.toString(CharsetUtil.UTF_8)!!
            requestResponse.content += message
            if (content is LastHttpContent) {
                val handlerExtension : Response.() -> Unit = callback
                requestResponse.handlerExtension()
            }
        }
    }

}