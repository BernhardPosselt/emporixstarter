package at.fyayc.backend.auth

import org.springframework.stereotype.Controller

data class Credentials(
    val token: String,
)

@Controller("/login")
class LoginController {
    fun login(credentials: Credentials) {
//        if()
    }
}