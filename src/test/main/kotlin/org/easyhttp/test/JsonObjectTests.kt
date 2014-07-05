package org.easyHttp.test

import org.junit.Test as spec
import com.google.gson.reflect.TypeToken
import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap
import com.google.gson.JsonObject
import com.google.gson.JsonElement
import kotlin.test.assertEquals
import org.easyHttp.json
import kotlin.test.failsWith
import org.easyHttp.InvalidPathException


public class JsonObjectTests() {

    spec fun requesting_object_should_return_entire_object() {

        val content = "{\"response\":{\"status\":{\"version\":\"4.2\",\"code\":0,\"message\":\"Success\"},\"artists\":[{\"id\":\"ARH6W4X1187B99274F\",\"name\":\"Radiohead\"},{\"id\":\"ARUMMEI1431609FC94\",\"name\":\"Radiohead+Mojib\"}]}}"

        val gson = Gson()
        val input = gson.fromJson(content , javaClass<JsonObject>());

        assertEquals("{\"status\":{\"version\":\"4.2\",\"code\":0,\"message\":\"Success\"},\"artists\":[{\"id\":\"ARH6W4X1187B99274F\",\"name\":\"Radiohead\"},{\"id\":\"ARUMMEI1431609FC94\",\"name\":\"Radiohead+Mojib\"}]}", input?.json("response").toString())


    }

    spec fun requesting_simple_property_should_return_property() {

        val content = "{\"response\":{\"status\":{\"version\":\"4.2\",\"code\":0,\"message\":\"Success\"},\"artists\":[{\"id\":\"ARH6W4X1187B99274F\",\"name\":\"Radiohead\"},{\"id\":\"ARUMMEI1431609FC94\",\"name\":\"Radiohead+Mojib\"}]}}"

        val gson = Gson()
        val input = gson.fromJson(content , javaClass<JsonObject>());

        assertEquals("{\"version\":\"4.2\",\"code\":0,\"message\":\"Success\"}",input?.json("response.status").toString())



    }

    spec fun requesting_complex_property_should_return_property() {

        val content = "{\"response\":{\"status\":{\"version\":\"4.2\",\"code\":0,\"message\":\"Success\"},\"artists\":[{\"id\":\"ARH6W4X1187B99274F\",\"name\":\"Radiohead\"},{\"id\":\"ARUMMEI1431609FC94\",\"name\":\"Radiohead+Mojib\"}]}}"

        val gson = Gson()
        val input = gson.fromJson(content , javaClass<JsonObject>());

        assertEquals("\"4.2\"", input?.json("response.status.version").toString())


    }

    spec fun requesting_array_should_return_array() {

        val content = "{\"response\":{\"status\":{\"version\":\"4.2\",\"code\":0,\"message\":\"Success\"},\"artists\":[{\"id\":\"ARH6W4X1187B99274F\",\"name\":\"Radiohead\"},{\"id\":\"ARUMMEI1431609FC94\",\"name\":\"Radiohead+Mojib\"}]}}"

        val gson = Gson()
        val input = gson.fromJson(content , javaClass<JsonObject>());

        assertEquals("[{\"id\":\"ARH6W4X1187B99274F\",\"name\":\"Radiohead\"},{\"id\":\"ARUMMEI1431609FC94\",\"name\":\"Radiohead+Mojib\"}]",input?.json("response.artists").toString())

    }


    spec fun requesting_specific_array_entry_should_return_object() {

        val content = "{\"response\":{\"status\":{\"version\":\"4.2\",\"code\":0,\"message\":\"Success\"},\"artists\":[{\"id\":\"ARH6W4X1187B99274F\",\"name\":\"Radiohead\"},{\"id\":\"ARUMMEI1431609FC94\",\"name\":\"Radiohead+Mojib\"}]}}"

        val gson = Gson()
        val input = gson.fromJson(content , javaClass<JsonObject>());

        assertEquals("{\"id\":\"ARH6W4X1187B99274F\",\"name\":\"Radiohead\"}", input?.json("response.artists[0]").toString())


    }
    spec fun requesting_specific_array_entry_property_should_return_property() {

        val content = "{\"response\":{\"status\":{\"version\":\"4.2\",\"code\":0,\"message\":\"Success\"},\"artists\":[{\"id\":\"ARH6W4X1187B99274F\",\"name\":\"Radiohead\"},{\"id\":\"ARUMMEI1431609FC94\",\"name\":\"Radiohead+Mojib\"}]}}"

        val gson = Gson()
        val input = gson.fromJson(content , javaClass<JsonObject>());

        assertEquals("\"Radiohead\"", input?.json("response.artists[0].name").toString())


    }

    spec fun requesting_unknown_property_should_throw_exception() {
        val content = "{\"response\":{\"status\":{\"version\":\"4.2\",\"code\":0,\"message\":\"Success\"},\"artists\":[{\"id\":\"ARH6W4X1187B99274F\",\"name\":\"Radiohead\"},{\"id\":\"ARUMMEI1431609FC94\",\"name\":\"Radiohead+Mojib\"}]}}"

        val gson = Gson()
        val input = gson.fromJson(content , javaClass<JsonObject>());

        failsWith(javaClass<InvalidPathException>(), {
            input?.json("response1").toString()
        })

    }
    spec fun requesting_unknown_complex_property_should_throw_invalidpathexception() {
        val content = "{\"response\":{\"status\":{\"version\":\"4.2\",\"code\":0,\"message\":\"Success\"},\"artists\":[{\"id\":\"ARH6W4X1187B99274F\",\"name\":\"Radiohead\"},{\"id\":\"ARUMMEI1431609FC94\",\"name\":\"Radiohead+Mojib\"}]}}"

        val gson = Gson()
        val input = gson.fromJson(content , javaClass<JsonObject>());

        failsWith(javaClass<InvalidPathException>(), {
            input?.json("response.status1").toString()
        })

    }
    spec fun requesting_array_out_of_bounds_entry_should_throw_indexoutofboundsexception() {

        val content = "{\"response\":{\"status\":{\"version\":\"4.2\",\"code\":0,\"message\":\"Success\"},\"artists\":[{\"id\":\"ARH6W4X1187B99274F\",\"name\":\"Radiohead\"},{\"id\":\"ARUMMEI1431609FC94\",\"name\":\"Radiohead+Mojib\"}]}}"

        val gson = Gson()
        val input = gson.fromJson(content , javaClass<JsonObject>());

        failsWith(javaClass<IndexOutOfBoundsException>(), {
            input?.json("response.artists[10].name").toString()
        })

    }
    spec fun requesting_unknown_property_of_array_should_throw_invalidpathexception() {
        val content = "{\"response\":{\"status\":{\"version\":\"4.2\",\"code\":0,\"message\":\"Success\"},\"artists\":[{\"id\":\"ARH6W4X1187B99274F\",\"name\":\"Radiohead\"},{\"id\":\"ARUMMEI1431609FC94\",\"name\":\"Radiohead+Mojib\"}]}}"

        val gson = Gson()
        val input = gson.fromJson(content , javaClass<JsonObject>());

        failsWith(javaClass<InvalidPathException>(), {
            input?.json("response.artists[0].email").toString()
        })

    }
}

