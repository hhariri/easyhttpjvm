package org.easyHttp

import com.google.gson.Gson


public class JsonDecoder : ContentDecoder("application/json", "application/json;charset=\\w*\\W*\\w*") {
    override fun decode<T>(input: String, objectType: Class<T>): T {
        val gson = Gson()
        return (gson.fromJson<T>(input, objectType) as T)
    }

}