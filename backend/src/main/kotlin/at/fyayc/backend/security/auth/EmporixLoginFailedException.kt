package at.fyayc.backend.security.auth

import org.springframework.security.core.AuthenticationException

class EmporixLoginFailedException : AuthenticationException {
    constructor(msg: String, cause: Throwable) : super(msg, cause)
    constructor(msg: String) : super(msg)
}