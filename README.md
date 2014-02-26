## EasyHttp.JVM

A port of [EasyHttp](http://github.com/hhariri/EasyHttp) for the JVM based on Netty.

**WARNING**: Work in progress.

## Usage

Create an instance of EasyHttp, invoke a verb with a url, some header info and a callback function.
#### A GET


{% highlight kotlin %}
val http = EasyHttp()

http.get("http://echo.jsontest.com/name/joe/email/joe@gmail.com",
        callback = {
            val customer = content(javaClass<CustomerSimpleClass>())
        }
)
{% endhighlight %}

#### A POST
{% highlight kotlin %}
val http = EasyHttp()

http.post(
    url = "http://httpbin.org/post",
    contents = hashMapOf(Pair("name", "Joe"), Pair("email", "joe@gmail.com")),
        headers = Headers(contentType = "application/x-www-form-urlencoded"),
        callback = {
            // do something with response here...
       }
)
{% endhighlight %}

## Features

* GET, POST, PUT, OPTIONS, DELETE, PATCH
* Serialization and Deserialization of JSON. You can post/put JSON as well as get the response as JSOn
* Support for application/form-urlencoded
* Most headers, including cookies


## Work in Progress

Quite a lot left

* More headers
* Support for Rx (planned)
* Improve tests and move to [Spek](http://jetbrains.github.io/spek)
* Add support for XML serialization/deserialization
* Clean up code



