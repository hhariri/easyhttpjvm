package org.easyHttp

import java.lang.reflect.Method
import java.util.ArrayList
import java.beans.Introspector


fun Class<out Any>.propertyGetter(property: String): Method {
    try {
        return getMethod("get${when {
            property.length == 1 && property[0].isLowerCase() -> property.capitalize()
            property.length > 2 && property[1].isLowerCase() -> property.capitalize()
            else -> property
        }}")
    }
    catch (e: Exception) {
        throw Exception("invalid $property")
    }
}
fun Any.propertyValue(property: String): Any? {
    val getter = javaClass.propertyGetter(property)
    return getter.invoke(this)
}

fun Class<out Any>.properties(): List<String> {
    val answer = ArrayList<String>()

    for (method in getDeclaredMethods()) {
        val name = method.getName()!!
        if (name.startsWith("get") && method.getParameterTypes()?.size == 0) {
            answer.add(Introspector.decapitalize(name.substring(3))!!)
        }
    }

    return answer.sort()
}

fun convertClassToKeyValue(data: Any): Map<String, *> {
    val keyValues = hashMapOf<String, Any>()
    for (propertyName in data.javaClass.properties()) {
        keyValues.set(propertyName, data.propertyValue(propertyName)!!)
    }
    return keyValues
}

