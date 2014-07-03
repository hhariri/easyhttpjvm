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
import kotlin.Map
import java.util.ArrayList
import rx.Observable
import rx.subjects.Subject
import rx.subjects.PublishSubject
import rx.subjects.ReplaySubject
import rx.Subscriber
import rx.Observable.OnSubscribe

public class EasyHttp(private val enableLogging: Boolean = false,
                      val streamers: List<Streamer> = listOf(FormStreamer(), BodyStreamer()),
                      val decoders: List<ContentDecoder> = listOf(JsonDecoder()),
                      val encoders: List<ContentEncoder> = listOf(
                                                                    ApplicationUrlFormEncoder(),
                                                                    JsonEncoder(),
                                                                    TextPlainEncoder())
                        )
{




    private fun setupBootStrap(eventLoopGroup: NioEventLoopGroup, callback: Response.() -> Unit): Bootstrap {
        val bootStrap = Bootstrap()
        bootStrap.group(eventLoopGroup)
                ?.channel(javaClass<NioSocketChannel>())
                ?.handler(HttpClientInitializer(enableLogging = false, useSSL = false, callback = callback, deserializers = decoders))
        return bootStrap
    }

    private fun convertToNettyCookies(cookies: ArrayList<Cookie>): ArrayList<io.netty.handler.codec.http.Cookie> {

        val nettyCookies = arrayListOf<io.netty.handler.codec.http.Cookie>()
        cookies.forEach {
            val nettyCookie = DefaultCookie(it.name, it.value)
            nettyCookie.setMaxAge(it.maxAge)
            nettyCookie.setDomain(it.domain)
            nettyCookie.setPath(it.path)
            nettyCookie.setSecure(it.isSecure)
            nettyCookies.add(nettyCookie)
        }
        return nettyCookies
    }

    fun executeRequest(url: String, headers: Headers, method: HttpMethod, callback: Response.() -> Unit, contents: Any? = null) {
        val eventLoopGroup = NioEventLoopGroup()
        try {
            val bootstrap = setupBootStrap(eventLoopGroup, callback)
            val uri = URI(url)
            val host = uri.getHost() ?: "localhost"
            var port = uri.getPort()
            if (port == -1) {
                port = 80;
            }
            val rawPath = "${URI(url).getRawPath()}?${URI(url).getRawQuery()}"
            var request: HttpRequest
            var contentLength = 0


            if ((method == HttpMethod.POST || method == HttpMethod.PATCH) && contents != null) {
                val encoder = encoders.find { it.canEncode(headers.contentType)}
                if (encoder != null) {
                    val encodedData = encoder.encode(contents)
                    val streamer = streamers.firstOrNull { it.canStream(headers.contentType)}
                    if (streamer != null) {
                        val streamerResponse = streamer.streamData(encodedData, method, rawPath!!)
                        contentLength = streamerResponse.contentLength
                        request = streamerResponse.request
                    } else {
                        throw StreamerException("Streamer not found")
                    }
                } else {
                    throw EncoderException("Cannot find encoder for content type")
                }
            } else {
                request = DefaultFullHttpRequest(HttpVersion.HTTP_1_1, method, rawPath)
            }

            val requestHeaders = request.headers()!!
            if (headers.contentType != ""){
                requestHeaders.set(HttpHeaders.Names.CONTENT_TYPE, headers.contentType)
            }
            if (contentLength > 0) {
                requestHeaders.set(HttpHeaders.Names.CONTENT_LENGTH, contentLength)
            }

            requestHeaders.set(HttpHeaders.Names.HOST, host)
            requestHeaders.set(HttpHeaders.Names.ACCEPT, headers.accept)
            requestHeaders.set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.CLOSE)
            requestHeaders.set(HttpHeaders.Names.ACCEPT_ENCODING, HttpHeaders.Values.GZIP)
            requestHeaders.set(HttpHeaders.Names.ACCEPT_CHARSET, headers.acceptCharSet)
            requestHeaders.set(HttpHeaders.Names.FROM, headers.from)
            requestHeaders.set(HttpHeaders.Names.ACCEPT_LANGUAGE, headers.acceptLanguage)
            requestHeaders.set(HttpHeaders.Names.USER_AGENT, headers.userAgent)
            requestHeaders.set(HttpHeaders.Names.COOKIE, ClientCookieEncoder.encode(convertToNettyCookies(headers.cookies)))
            val ch = bootstrap.connect(host, port)!!.sync()!!.channel()!!
            ch.writeAndFlush(request)

            ch.closeFuture()?.sync()
        } finally {
            eventLoopGroup.shutdownGracefully()
        }
    }

    fun<T> createObservable(onSubscribe : (Subscriber<in T>) -> Unit) : Observable<T> {
        return Observable.create(object : OnSubscribe<T> {
            override fun call(s: Subscriber<in T>?) {
                onSubscribe(s!!)
            }
        })!!
    }

    fun get(url: String, headers: Headers = Headers(), callback: Response.() -> Unit) {
        executeRequest(url, headers, HttpMethod.GET, callback)
    }

    fun get(url: String, headers: Headers = Headers()): Observable<Response> {
        return Observable.create(object: OnSubscribe<Response> {
            override fun call(s: Subscriber<in Response>?) {
                get(url, headers, {
                    s?.onNext(this)
                    s?.onCompleted()
                })
            }
        })!!
    }

    fun post(url: String, headers: Headers = Headers(), contents: Any? = null, callback: Response.() -> Unit) {
        executeRequest(url, headers, HttpMethod.POST, callback, contents)
    }

    fun post(url: String, headers: Headers = Headers(), contents: Any? = null): Observable<Response> {
        return createObservable<Response> {
            post(url, headers, contents, {
                it.onNext(this)
                it.onCompleted()
            })
        }
    }

    fun head(url: String, headers: Headers = Headers(), callback: Response.() -> Unit) {
        executeRequest(url, headers, HttpMethod.HEAD, callback)
    }

    fun head(url: String, headers: Headers = Headers()): Observable<Response> {
        return createObservable {
            head(url, headers, {
                it.onNext(this)
                it.onCompleted()
            })
        }
    }

    fun options(url: String, headers: Headers = Headers(), callback: Response.() -> Unit) {
        executeRequest(url, headers, HttpMethod.OPTIONS, callback)
    }

    fun options(url: String, headers: Headers = Headers()): Observable<Response> {
        return createObservable {
            options(url, headers, {
                it.onNext(this)
                it.onCompleted()
            })
        }
    }
    fun put(url: String, headers: Headers = Headers(), contents: Any? = null, callback: Response.() -> Unit) {
        executeRequest(url, headers, HttpMethod.PUT, callback, contents)
    }

    fun put(url: String, headers: Headers = Headers(), contents: Any? = null): Observable<Response> {
        return createObservable<Response> {
            put(url, headers, contents, {
                it.onNext(this)
                it.onCompleted()
            })
        }
    }

    fun delete(url: String, headers: Headers = Headers(), callback: Response.() -> Unit) {
        executeRequest(url, headers, HttpMethod.DELETE, callback)
    }

    fun delete(url: String, headers: Headers = Headers()): Observable<Response> {
        return createObservable<Response> {
            delete(url, headers, {
                it.onNext(this)
                it.onCompleted()
            })
        }
    }
    fun patch(url: String, headers: Headers = Headers(), callback: Response.() -> Unit) {
        executeRequest(url, headers, HttpMethod.PATCH, callback)
    }



}

