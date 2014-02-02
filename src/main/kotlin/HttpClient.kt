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

public class HttpClient() {


    fun get(url: String, callback: (String) -> Unit) {
        val uri = URI(url)
        val eventLoopGroup = NioEventLoopGroup()
        val host = (if (uri.getHost() == null)
            "localhost"
        else
            uri.getHost())

        try {
            val bootstrap = Bootstrap()
            bootstrap.group(eventLoopGroup)
            ?.channel(javaClass<NioSocketChannel>())
            ?.handler(HttpClientInitializer(false, callback))
            // Make the connection attempt.
            val ch = bootstrap.connect(url, 80)!!.sync()!!.channel()!!

            // Prepare the HTTP request.
            val request = DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, uri.getRawPath())
            request.headers()!!.set(HttpHeaders.Names.HOST, host)
            request.headers()!!.set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.CLOSE)
            request.headers()!!.set(HttpHeaders.Names.ACCEPT_ENCODING, HttpHeaders.Values.GZIP)

            // Set some example cookies.
            request.headers()!!.set(HttpHeaders.Names.COOKIE, ClientCookieEncoder.encode(DefaultCookie("my-cookie", "foo"), DefaultCookie("another-cookie", "bar")))

            // Send the HTTP request.
            ch.writeAndFlush(request)

            // Wait for the server to close the connection.
            ch.closeFuture()!!.sync()
        } finally {
            // Shut down executor threads to exit.
            eventLoopGroup.shutdownGracefully();
        }

    }

}

