package org.easyHttp

import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.codec.http.HttpObject
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.http.HttpHeaders
import io.netty.handler.codec.http.HttpContent
import io.netty.util.CharsetUtil
import io.netty.handler.codec.http.LastHttpContent
import io.netty.handler.codec.http.HttpResponse
import java.util.SortedMap
import java.util.Date
import org.joda.time.format.DateTimeFormatter
import org.joda.time.format.DateTimeFormat
import org.joda.time.DateTime


class HttpClientHandler(private val callback: Response.() -> Unit, private val deserializers: List<ContentDecoder>): SimpleChannelInboundHandler<HttpObject>() {

    var contentValue = ""
    var age: String? = null
    var accessControlAllowOrigin: String? = null
    var accept: SortedMap<String,Int>? = null
    var acceptRanges: String? = null
    var allow: String? = null
    var cacheControl: String? = null
    var connection: String? = null
    var content: String? = null
    var contentEncoding: String? = null
    var contentLanguage: String? = null
    var contentLength: Int = 0
    var contentDisposition: String? = null
    var contentType: ContentType? = null
    var date: DateTime? = null
    var eTag: String? = null
    var expires: DateTime? = null
    var lastModified: DateTime? = null
    var location: String? = null
    var pragma: String? = null
    var proxyAuthenticate: String? = null
    var server: String? = null
    var statusText: String = "Undefined"
    var statusCode: StatusCode = StatusCode.Undefined


    fun getHeader(headers: HttpHeaders?, name: String): String? {
        if (headers != null) {
            return headers.get(name)
        }
        return null
    }

    override fun channelRead0(ctx: ChannelHandlerContext?, msg: HttpObject?) {
        if (msg is HttpResponse) {
            val response = (msg as HttpResponse)
            statusCode = numberToStatusCode(response.getStatus()!!.code())
            statusText = response.getStatus()!!.reasonPhrase()!!
            age = getHeader(msg.headers(), "Age")
            accessControlAllowOrigin = getHeader(msg.headers(), "Access-Control-Allow-Origin")
            accept = parseWeightedHeaders(getHeader(msg.headers(), "Accept") ?: "")
            acceptRanges = getHeader(msg.headers(), "Accept-Ranges")
            allow = getHeader(msg.headers(), "Allow")
            cacheControl = getHeader(msg.headers(), "Cache-Control")
            connection = getHeader(msg.headers(), "Connection")
            contentLength = getHeader(msg.headers(), "Content-Length")?.toInt() ?: 0
            contentEncoding = getHeader(msg.headers(), "Content-Encoding")
            val contentTypeHeader = getHeader(msg.headers(), "Content-Type")
            if (contentTypeHeader != null && contentTypeHeader != "") {
                contentType = ContentType.parse(contentTypeHeader)
            }

            location = getHeader(msg.headers(), "Location")
            server = getHeader(msg.headers(), "Server")
            val headerDate = getHeader(msg.headers(), "Date")
            if (headerDate != null) {
                date = DateTimeFormat.forPattern("EEE, dd MMM yyyy HH:mm:ss zzz")!!.parseDateTime(getHeader(msg.headers(), "Date"))
            }
        }
        if (msg is HttpContent) {
            val content = (msg as HttpContent)
            val message = content.content()!!.toString(CharsetUtil.UTF_8)!!
            contentValue += message
            if (content is LastHttpContent) {
                val handlerExtension : Response.() -> Unit = callback
                Response(
                        deserializers,
                        statusCode = statusCode,
                        statusText = statusText,
                        content = contentValue,
                        age = age,
                        cacheControl = cacheControl,
                        allow = allow,
                        accessControlAllowOrigin = accessControlAllowOrigin,
                        contentType = contentType,
                        contentLength = contentLength,
                        location = location,
                        connection = connection,
                        date = date,
                        server = server
                ).handlerExtension()
            }
        }
    }

}