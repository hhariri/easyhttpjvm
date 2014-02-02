package org.easyHttp

import io.netty.bootstrap.Bootstrap
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.channel.socket.SocketChannel
import io.netty.channel.ChannelInitializer
import io.netty.handler.logging.LoggingHandler
import io.netty.handler.logging.LogLevel
import io.netty.handler.ssl.SslHandler
import io.netty.handler.codec.http.HttpClientCodec
import io.netty.handler.codec.http.HttpContentDecompressor
import io.netty.handler.codec.http.HttpObject
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.http.HttpContent
import io.netty.util.CharsetUtil
import io.netty.handler.codec.http.LastHttpContent
import io.netty.handler.codec.http.HttpHeaders
import io.netty.handler.codec.http.HttpResponse
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.handler.codec.http.DefaultCookie
import io.netty.handler.codec.http.ClientCookieEncoder
import io.netty.handler.codec.http.HttpMethod
import io.netty.handler.codec.http.HttpVersion
import io.netty.handler.codec.http.DefaultFullHttpRequest
import java.net.URI


fun main(args: Array<String>) {

    val httpClient = HttpClient()

    httpClient.get("www.w3schools.com", ::handler)

    httpClient.get("www.google.com", { response -> Unit
        println(response)
    })
}

fun handler(message: String): Unit {
    println("Response is $message")
}