package org.easyHttp.test

import org.easyHttp.EasyHttp
import rx.Subscriber
import org.easyHttp.Response
import rx.Observable


fun main(args: Array<String>) {

    val http = EasyHttp()






    val o1 = http.get("http://www.google.com")
    val o2 = http.get("http://www.google.com")

    Observable.combineLatest(o1, o2).subscribe() {
        println("Badabing")
    }





}