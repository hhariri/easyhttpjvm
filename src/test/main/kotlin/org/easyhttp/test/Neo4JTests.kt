package org.easyHttp.test.tmp

import org.easyHttp.EasyHttp
import org.easyHttp.Headers
import org.easyHttp.ContentType
import java.util.ArrayList
import org.joda.time.DateTime
import org.easyHttp.StatusCode
import com.google.gson.reflect.TypeToken
import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap
import com.google.gson.JsonObject
import com.google.gson.JsonElement

public class CypherStatement(val serverUrl: String, val transactionTimeout: Int = 60) {

    var globalTransactionUrl = ""

    public fun createNode(node: Any): String {
        val http = EasyHttp()

        var url = "${serverUrl}/db/data/transaction"
        if (globalTransactionUrl != "") {
            url = globalTransactionUrl
        }
        http.post(url,
                headers = Headers(contentType = "application/json", accept = "application/json", acceptCharSet = "UTF-8"),
                contents = "{ \"statements\": [{\"statement\": \"CREATE (n:Developer { name: 'Andres10', title: 'Developer'})\"}]}",
                callback = {
                    println("callback finished")
                    globalTransactionUrl = location ?: ""
                })
        return ""
    }

}
public class EasyNeo4J(val serverUrl: String = "http://localhost:7474", val transactionTimeout: Int = 60) {


    fun transaction(statements: CypherStatement.() -> Unit) {
        val h: CypherStatement.() -> Unit = statements
        val s = CypherStatement(serverUrl, transactionTimeout)

        // need to start transaction here

        s.h()

        // need to wait and end all transactions here.

        val http = EasyHttp()
        println("calling terminate tx")
        http.post("${s.globalTransactionUrl}/commit", callback = {
            println("in tx callback")
        })
    }

}

data class Params(val props: Any)

data class Statement(val statement: String, val parameters: Params)

data class Wrapper(val statements: ArrayList<Statement>)

data class Customer(val name: String, val dateTime: Long)


data class Neo4JConstraint(val label: String, val property: String)

data class Neo4JConstraintProperty(val property_keys: String)

data class Neo4JConstraintCause(val message: String, val exception: String, val stacktrace: ArrayList<String>, val fullname: String)

data class Neo4JConstraintResult(val message: String, val exception: String, val stacktrace: ArrayList<String>, val fullname: String, val cause: Neo4JConstraintCause)

fun main(args: Array<String>) {

    val http = EasyHttp()
/*    val props = Neo4JConstraintProperty("Credentials")
    val observable = http.post("http://localhost:7474/db/data/schema/constraint/email/uniqueness/", Headers(contentType = ContentType.Application.Json.toString()), props).toBlockingObservable()
    observable?.forEach {
        if (it?.statusCode != StatusCode.OK) {
            val result = it?.content(javaClass<Neo4JConstraintResult>())!!
            println(result)
        }
    }


    val observable1 = http.delete("http://localhost:7474/db/data/schema/constraint/Credentials/uniqueness/email").toBlockingObservable()
    observable1?.forEach {

        println(it?.content)
    }*/




}




