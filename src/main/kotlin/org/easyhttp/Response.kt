package org.easyHttp

import java.util.Date
import java.util.SortedMap
import org.joda.time.DateTime
import com.google.gson.JsonElement
import com.google.gson.Gson
import com.google.gson.JsonObject


public class Response(
        private val deserializers: List<ContentDecoder>,
        val age: String? = null,
        val accessControlAllowOrigin: String? = null,
        val accept: SortedMap<String,Int>? = null,
        val acceptRanges: String? = null,
        val allow: String? = null,
        val cacheControl: String? = null,
        val connection: String? = null,
        val content: String? = null,
        val contentEncoding: String? = null,
        val contentLanguage: String? = null,
        val contentLength: Int,
        val contentDisposition: String? = null,
        val contentType: ContentType? = null,
        val date: DateTime? = null,
        val eTag: String? = null,
        val expires: DateTime? = null,
        val lastModified: DateTime? = null,
        val location: String? = null,
        val pragma: String? = null,
        val proxyAuthenticate: String? = null,
        val server: String? = null,
        val statusCode: StatusCode,
        val statusText: String) {


    fun content<T>(objectType: Class<T>): T {
        val deserializer = deserializers.firstOrNull { it.canDecode(contentType.toString())}
        if (deserializer != null) {
            return (deserializer.decode<T>(content!!, objectType) as T)
        }
        throw Exception("Cannot find correct deserializer for given content type")
    }

    fun contentAsJson(path: String): JsonElement {
        val gson = Gson()
        val output = gson.fromJson(content , javaClass<JsonObject>())
        return output?.json(path)!!
    }

    fun contentAsJsonObject(): JsonObject {
        val gson = Gson()
        val output = gson.fromJson(content, javaClass<JsonObject>())
        return output!!
    }

}




fun parseWeightedHeaders(header: String): SortedMap<String, Int> {

    val parsed = hashMapOf<String, Int>()
    val entries = header.split(',')
    for (entry in entries) {
        val parts = entry.split(';')
        val mediaType = parts[0]
        var weight = 1
        if (parts.size == 2) {
            val float = parts[1].drop(2).toFloat() * 10
            weight = float.toInt()
        }
        parsed.put(mediaType, weight)
    }
    return parsed.toSortedMap<String, Int>()
}

