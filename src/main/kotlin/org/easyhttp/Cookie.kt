package org.easyHttp

data class Cookie(val name: String,
                  val value: String,
                  val maxAge: Long = 0,
                  val isSecure: Boolean = false,
                  val domain: String = "",
                  val path: String = "/")