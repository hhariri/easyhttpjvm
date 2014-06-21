package org.easyHttp.test.tmp

import org.easyHttp.EasyHttp
import org.easyHttp.Headers
import org.easyHttp.ContentType
import java.util.ArrayList
import org.joda.time.DateTime

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
fun main(args: Array<String>) {

    val http = EasyHttp()

    val data = Customer("JoeTesting", DateTime().getMillis())
    val wrapper = Wrapper(arrayListOf(Statement("CREATE (n:Course {props}) RETURN n", Params(data))))

    http.post("http://localhost:7474/db/data/transaction/commit", Headers(accept = ContentType.Application.Json.toString(), contentType = ContentType.Application.Json.toString()), wrapper, {
        println(statusCode)
        println(content)
    });

}


