package at.fyayc.backend.security.auth

class CustomerTokenRefreshFailed : RuntimeException {
    constructor() : super()
    constructor(throwable: Throwable) : super(throwable)
}