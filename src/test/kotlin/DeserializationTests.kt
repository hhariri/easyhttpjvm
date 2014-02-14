package org.easyHttp.test


import org.junit.Test as spec
import org.easyHttp.EasyHttp
import kotlin.test.assertNotNull
import org.easyHttp.JsonDecoder
import com.google.gson.Gson
import kotlin.test.assertEquals

data class CustomerAddressClass(val street: String, val number: Int, val city: String)

data class CustomerClass(val name: String, val email: String, val address: CustomerAddressClass)


class DeserializationTests {




    spec fun valid_json_should_deserialize() {

        val jsonDeserializer = JsonDecoder()

        val customer = jsonDeserializer.decode("{name: \"Joe\", email: \"joe@smith.com\", address: {street:\"Street\", number: 10, city: \"Lonodon\"}}", javaClass<CustomerClass>())

        assertEquals(10, customer.address.number)
    }


}