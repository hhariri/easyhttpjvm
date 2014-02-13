package org.easyHttp

import com.google.gson.Gson


public class JsonDeserializer: ContentDeserializer("application/json", "application/json;charset=\\w*\\W*\\w*") {
    override fun deserialize<T>(input: String, objectType: Class<T>): T {
        val gson = Gson()
        return (gson.fromJson<T>(input, objectType) as T)
    }

}