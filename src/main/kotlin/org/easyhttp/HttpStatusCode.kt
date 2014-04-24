package org.easyHttp

enum class StatusCode(val value: Int) {
    open val description: String
        get() = this.toString().replaceAll("([A-Z])", " $1")

    Undefined: StatusCode(0)

    Continue : StatusCode(100)
    SwitchingProtocols : StatusCode(101)
    Processing : StatusCode(102)

    OK : StatusCode(200)
    Created : StatusCode(201)
    Accepted : StatusCode(202)
    NonAuthoritativeInformation : StatusCode(203)
    NoContent : StatusCode(204)
    ResetContent : StatusCode(205)
    PartialContent : StatusCode(206)

    // 3XX
    MultipleChoices : StatusCode(300)
    MovedPermanently : StatusCode(301)
    Found : StatusCode(302)
    SeeOther : StatusCode(303)
    NotModified : StatusCode(304)
    UseProxy : StatusCode(305)
    SwitchProxy : StatusCode(306)
    TemporaryRedirect : StatusCode(307)
    PermanentRedirect : StatusCode(308)

    // 4XX
    BadRequest : StatusCode(400)
    Unauthorized : StatusCode(401)
    PaymentRequired : StatusCode(402)
    Forbidden : StatusCode(403)
    NotFound : StatusCode(404)
    MethodNotAllowed : StatusCode(405)
    NotAcceptable : StatusCode(406)
    ProxyAuthenticationRequired : StatusCode(407)
    RequestTimeout : StatusCode(408)
    Conflict : StatusCode(409)
    Gone : StatusCode(410)
    LengthRequired : StatusCode(411)
    PreconditionFailed : StatusCode(412)
    RequestEntityTooLarge : StatusCode(413)
    RequestURITooLarge : StatusCode(414) {
        override val description: String = "Request-URI Too Large"
    }
    UnsupportedMediaType : StatusCode(415)
    RequestedRageNotSatisfiable : StatusCode(416)
    ExceptionFailed : StatusCode(417)
    TooManyRequests : StatusCode(429)
    RequestHeaderFieldTooLarge : StatusCode(431)

    // 5XX
    InternalServerError : StatusCode(500)
    NotImplemented : StatusCode(501)
    BadGateway : StatusCode(502)
    ServiceUnavailable : StatusCode(503)
    GatewayTimeout : StatusCode(504)
    VersionNotSupported : StatusCode(505)
    VariantAlsoNegotiates : StatusCode(506)
    InsufficientStorage : StatusCode(507)
    BandwidthLimitExceeded : StatusCode(509)
}

fun numberToStatusCode(number: Int): StatusCode {
    when (number) {
        100 -> return StatusCode.Continue
        101 -> return StatusCode.SwitchingProtocols
        102 -> return StatusCode.Processing
        200 -> return StatusCode.OK
        201 -> return StatusCode.Created
        202 -> return StatusCode.Accepted
        203 -> return StatusCode.NonAuthoritativeInformation
        204 -> return StatusCode.NoContent
        205 -> return StatusCode.ResetContent
        206 -> return StatusCode.PartialContent
        300 -> return StatusCode.MultipleChoices
        301 -> return StatusCode.MovedPermanently
        302 -> return StatusCode.Found
        303 -> return StatusCode.SeeOther
        304 -> return StatusCode.NotModified
        305 -> return StatusCode.UseProxy
        306 -> return StatusCode.SwitchProxy
        307 -> return StatusCode.TemporaryRedirect
        308 -> return StatusCode.PermanentRedirect
        400 -> return StatusCode.BadRequest
        401 -> return StatusCode.Unauthorized
        402 -> return StatusCode.PaymentRequired
        403 -> return StatusCode.Forbidden
        404 -> return StatusCode.NotFound
        405 -> return StatusCode.MethodNotAllowed
        406 -> return StatusCode.NotAcceptable
        407 -> return StatusCode.ProxyAuthenticationRequired
        408 -> return StatusCode.RequestTimeout
        409 -> return StatusCode.Conflict
        410 -> return StatusCode.Gone
        411 -> return StatusCode.LengthRequired
        412 -> return StatusCode.PreconditionFailed
        413 -> return StatusCode.RequestEntityTooLarge
        414 -> return StatusCode.RequestURITooLarge
        415 -> return StatusCode.UnsupportedMediaType
        416 -> return StatusCode.RequestedRageNotSatisfiable
        417 -> return StatusCode.ExceptionFailed
        429 -> return StatusCode.TooManyRequests
        431 -> return StatusCode.RequestHeaderFieldTooLarge
        500 -> return StatusCode.InternalServerError
        501 -> return StatusCode.NotImplemented
        502 -> return StatusCode.BadGateway
        503 -> return StatusCode.ServiceUnavailable
        504 -> return StatusCode.GatewayTimeout
        505 -> return StatusCode.VersionNotSupported
        506 -> return StatusCode.VariantAlsoNegotiates
        507 -> return StatusCode.InsufficientStorage
        509 -> return StatusCode.BandwidthLimitExceeded
    }
    throw Exception("Unsupported Status Code")
}