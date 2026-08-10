package at.fyayc.backend.auth

import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping

data class Credentials(
    val token: String,
)

@Controller("/login")
class LoginController {
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun login(credentials: Credentials) {
//        if()
    }
}