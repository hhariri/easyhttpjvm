package org.easyHttp.test


import org.junit.Test as spec
import org.easyHttp.EasyHttp
import org.easyHttp.StatusCode
import kotlin.test.assertEquals
import kotlin.test.assertNull
import org.joda.time.format.DateTimeFormat
import org.joda.time.DateTime
import kotlin.test.assertNotNull

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
            assertEquals(7641, contentLength)
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



}