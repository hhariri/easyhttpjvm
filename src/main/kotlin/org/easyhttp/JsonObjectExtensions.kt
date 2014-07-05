package org.easyHttp

import com.google.gson.JsonObject
import com.google.gson.JsonElement


fun JsonObject.json(path: String): JsonElement {

    if (!path.contains(".") && !path.contains("]")) {
        if (this.has(path)) {
            return this.get(path)!!
        }
        throw InvalidPathException(path)
    }
    val next = path.substringAfter(".")
    val current = path.substringBefore(".")

    if (current.contains("]")) {
        val index = current.substring(current.indexOf('[') + 1, current.indexOf(']')).toInt()
        if (next != "") {
            return this.getAsJsonArray(current.substringBefore("["))?.get(index)?.getAsJsonObject()?.json(next)!!
        }
        return this.getAsJsonArray(current.substringBefore("["))?.get(index)!!
    }
    return this.getAsJsonObject(current)?.json(next)!!
}
