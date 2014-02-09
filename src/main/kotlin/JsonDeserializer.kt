package org.easyHttp

import com.google.gson.Gson


public class JsonDeserializer: ContentDeserializer() {
    override fun deserialize<T>(input: String, objectType: Class<T>): T {
        val gson = Gson()
        return (gson.fromJson<T>(input, objectType) as T)
    }

}