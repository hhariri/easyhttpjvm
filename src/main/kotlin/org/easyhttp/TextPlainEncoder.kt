package org.easyHttp

public class TextPlainEncoder : ContentEncoder("text/plain") {
    override fun encode(data: Any?): Any {
        when (data) {
            is String -> { return data }
            else -> { return data.toString() }
        }
    }
}