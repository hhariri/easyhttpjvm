package org.easyHttp.test


import org.junit.Test as spec
import org.easyHttp.EasyHttp
import org.easyHttp.StatusCode
import kotlin.test.assertEquals
import kotlin.test.assertNull
import org.joda.time.format.DateTimeFormat
import org.joda.time.DateTime
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import org.easyHttp.Headers
import org.easyHttp.ContentType
import io.netty.handler.codec.http.Cookie
import java.util.ArrayList

data class CustomerSimpleClass(val name: String, val email: String)


class HttpTests() {

    spec fun get_on_existing_resources_returns_resource() {

        val http = EasyHttp()

        http.get("http://httpbin.org/", callback = {
            assertNotNull(content)

        })


    }

    spec fun head_on_existing_resources_returns_necessary_headers() {

        val http = EasyHttp()

        http.head("http://httpbin.org", callback = {
            assertEquals(StatusCode.OK, statusCode)
            assertEquals(StatusCode.OK.toString(), statusText)
            assertEquals("*", accessControlAllowOrigin)
            assertEquals("text/html; charset=utf-8", contentType.toString())
            assertEquals("gunicorn/0.17.4", server)
            assertEquals(7700, contentLength)
            assertEquals("Close", connection)
            val currentDateTime = DateTime.now()
            assertEquals(currentDateTime?.dayOfMonth()?.get(), date?.dayOfMonth()?.get())
            assertEquals(currentDateTime?.monthOfYear()?.get(), date?.monthOfYear()?.get())
            assertEquals(currentDateTime?.year()?.get(), date?.year()?.get())
            assertEquals("", content)
        })
    }

    spec fun on_get_requesting_content_as_json_should_return_deserialized_json() {

        val http = EasyHttp()

        http.get("http://echo.jsontest.com/name/joe/email/joe@gmail.com", callback = {
            val customer = content(javaClass<CustomerSimpleClass>())

            assertEquals("joe", customer.name)
            assertEquals("joe@gmail.com", customer.email)

        })

    }
    spec fun on_post_with_class_and_form_encoding_it_should_post_data_as_part_of_contents() {

        val http = EasyHttp()

        http.post(
                url = "http://httpbin.org/post",
                contents = CustomerSimpleClass(name = "Joe", email = "joe@gmail.com"),
                headers = Headers(contentType = "application/x-www-form-urlencoded", accept = "text/html"),

                callback = {
                    assertEquals(StatusCode.OK, statusCode)
                    assertEquals(592, contentLength)
                }
        )
    }

    spec fun on_post_with_key_value_and_form_encoding_it_should_post_data_as_part_of_contents() {
        val http = EasyHttp()

        http.post(
            url = "http://httpbin.org/post",
            contents = hashMapOf(Pair("name", "Joe"), Pair("email", "joe@gmail.com")),
                headers = Headers(contentType = "application/x-www-form-urlencoded"),
                callback = {
                assertEquals(StatusCode.OK, statusCode)
                    assertEquals(586, contentLength)
              }

        )
    }

    spec fun on_post_with_class_and_json_encoding_it_should_post_data_as_part_of_contents() {
        val http = EasyHttp()

        http.post(
                url = "http://httpbin.org/post",
                contents = CustomerSimpleClass(name = "Joe", email = "joe@gmail.com"),
                headers = Headers(contentType = "application/json"),

                callback = {
                    assertEquals(StatusCode.OK, statusCode)
                    assertEquals(613, contentLength)
                }
        )
    }





}