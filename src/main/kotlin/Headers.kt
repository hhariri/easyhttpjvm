package org.easyHttp

import java.util.ArrayList

object CONSTANTS {
    val EasyHttpVersion = "0.1"
}
data public class Headers(val accept: String = "*/*",
                                 val acceptCharSet: String = "",
                                 val acceptLanguage: String = "",
                                 val userAgent: String = "EasyHttp.JVM v${CONSTANTS.EasyHttpVersion}" ,
                                 val host: String = "127.0.0.1",
                                 val from: String = "",
                                 val contentType: String = "",
                                 val cookies: ArrayList<Cookie> = arrayListOf<Cookie>())

