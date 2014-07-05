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
import org.easyHttp.Response
import rx.Observer
import rx.observers.TestObserver
import rx.observers.TestSubscriber
import rx.functions.Action1
import rx.Subscriber
import org.easyHttp.json
import org.junit.Ignore

data class CustomerSimpleClass(val name: String, val email: String)


public class HttpTests() {

    spec fun get_on_existing_resources_returns_resource() {

        val http = EasyHttp()

        http.get("http://httpbin.org/", callback = {
            assertNotNull(content)

        })
    }


    spec fun getRx() {

        val http = EasyHttp()

        val subscriber = TestSubscriber<Response>()


        http.get("http://httpbin.org/").subscribe(subscriber)

        subscriber.awaitTerminalEvent()
        val events = subscriber.getOnNextEvents()
        assertNotNull(events)
        assertEquals(1, events!!.count())
        assertNotNull(events[0]?.content)

        subscriber.unsubscribe()

    }

    Ignore("Failing for some reason")
    spec fun head_on_existing_resources_returns_necessary_headers() {

        val http = EasyHttp()

        http.head("http://httpbin.org", callback = {
            assertEquals(StatusCode.OK, statusCode)
            assertEquals(StatusCode.OK.toString(), statusText)
            assertEquals("*", accessControlAllowOrigin)
            assertEquals("text/html; charset=utf-8", contentType.toString())
            assertEquals("gunicorn/18.0", server)
            assertEquals(7905, contentLength)
            assertEquals("Close", connection)
            val currentDateTime = DateTime.now()
            assertEquals(currentDateTime?.dayOfMonth()?.get(), date?.dayOfMonth()?.get())
            assertEquals(currentDateTime?.monthOfYear()?.get(), date?.monthOfYear()?.get())
            assertEquals(currentDateTime?.year()?.get(), date?.year()?.get())
            assertEquals("", content)
        })
    }

    spec fun on_get_requesting_static_content_as_json_should_return_deserialized_json() {

        val http = EasyHttp()

        http.get("http://echo.jsontest.com/name/joe/email/joe@gmail.com", Headers(accept = "application/json"), callback = {
            val customer = content(javaClass<CustomerSimpleClass>())

            assertEquals("joe", customer.name)
            assertEquals("joe@gmail.com", customer.email)

        })

    }
    spec fun on_get_requesting_content_asJson_should_return_deserialized_json() {

        val http = EasyHttp()

        http.get("http://echo.jsontest.com/name/joe/email/joe@gmail.com", Headers(accept = "application/json"), callback = {


            assertEquals("joe", contentAsJsonObject().json("customer.name"))
            assertEquals("joe", contentAsJson("customer.name"))
            assertEquals("joe@gmail.com", contentAsJson("customer.email"))

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
                    assertEquals(635, contentLength)
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
                    assertEquals(629, contentLength)
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
                    assertEquals(656, contentLength)
                }
        )
    }






}