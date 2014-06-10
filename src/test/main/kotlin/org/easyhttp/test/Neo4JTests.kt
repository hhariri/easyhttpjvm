package org.easyHttp.test.tmp

import org.easyHttp.EasyHttp
import org.easyHttp.Headers

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

fun main(args: Array<String>) {

    val customer = object {
        val labels = arrayListOf("Customer")
        val name = "Hadi"
        val email = "Hadi@Hadi.com"
    }


    val graph = EasyNeo4J()

    graph.transaction {

        createNode(customer)
        createNode(customer)

    }



    //    println(EasyNeo4J().createNode("http://localhost:7474", customer))
}
