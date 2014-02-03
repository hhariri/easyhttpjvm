package org.easyHttp

import io.netty.channel.ChannelInitializer
import java.nio.channels.SocketChannel
import io.netty.channel.socket.SocketChannel
import io.netty.handler.logging.LoggingHandler
import io.netty.handler.logging.LogLevel
import io.netty.handler.codec.http.HttpClientCodec
import io.netty.handler.codec.http.HttpContentDecompressor

public class HttpClientInitializer(private val useSSL: Boolean, private val callback: Response.() -> Unit): ChannelInitializer<SocketChannel>() {
    override fun initChannel(ch: SocketChannel?) {
        val p = ch?.pipeline()!!

        p.addLast("log", LoggingHandler(LogLevel.INFO))
        // Enable HTTPS if necessary.
        if (useSSL) {
            //      val engine = SecureChatSslContextFactory.getClientContext().createSSLEngine()
            //      engine.setUseClientMode(true)

            //      p.addLast("ssl", SslHandler(engine))
        }

        p.addLast("codec", HttpClientCodec())

        // Remove the following line if you don't want automatic content decompression.
        p.addLast("inflater", HttpContentDecompressor())

        // Uncomment the following line if you don't want to handle HttpChunks.
        //p.addLast("aggregator", new HttpObjectAggregator(1048576));

        p.addLast("handler", HttpClientHandler(callback))
    }
}
