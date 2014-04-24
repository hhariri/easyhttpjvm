package org.easyHttp.test

import org.easyHttp.EasyHttp


fun main(args: Array<String>) {

    val http = EasyHttp()

    http.get("http://www.google.com").toBlockingObservable()?.forEach({

        println(it?.content)
    })
    http.get("http://www.google.com").toBlockingObservable()?.forEach({
        println(it?.content)
    })
    //subscribe( {
   //     println(it?.content)
    //})
   /* http.get("http:/www.google.com").subscribe( {

 //       println(it?.content)
    })*/




}