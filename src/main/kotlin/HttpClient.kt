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
import java.net.URL
import io.netty.handler.codec.http.HttpRequest

public class HttpClient() {




    private fun setupBootStrap(eventLoopGroup: NioEventLoopGroup, callback: Response.() -> Unit): Bootstrap {
        val bootStrap = Bootstrap()
        bootStrap.group(eventLoopGroup)
                ?.channel(javaClass<NioSocketChannel>())
                ?.handler(HttpClientInitializer(false, callback))
        return bootStrap
    }

    private fun setupHeaders(url: String): HttpRequest {
        val uri = URI(url)
        val host = uri.getHost() ?: "localhost"

        // Prepare the HTTP request.
        val request = DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, uri.getRawPath())
        request.headers()!!.set(HttpHeaders.Names.HOST, host)
   //     request.headers()!!.set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.CLOSE)
   //     request.headers()!!.set(HttpHeaders.Names.ACCEPT_ENCODING, HttpHeaders.Values.GZIP)

        // Set some example cookies.
     //   request.headers()!!.set(HttpHeaders.Names.COOKIE, ClientCookieEncoder.encode(DefaultCookie("my-cookie", "foo"), DefaultCookie("another-cookie", "bar")))

        return request
    }

    fun get(url: String, callback: Response.() -> Unit) {
        val eventLoopGroup = NioEventLoopGroup()
        try {
            val bootstrap = setupBootStrap(eventLoopGroup, callback)
            val request = setupHeaders(url)
            val uri = URI(url)
            val host = uri.getHost() ?: "localhost"
            var port = uri.getPort()
            if (port == -1) {
                port = 80;
            }

            val ch = bootstrap.connect(host, port)!!.sync()!!.channel()!!

            ch.writeAndFlush(request)
            ch.closeFuture()?.sync()
        } finally {
            eventLoopGroup.shutdownGracefully()
        }
    }
}

