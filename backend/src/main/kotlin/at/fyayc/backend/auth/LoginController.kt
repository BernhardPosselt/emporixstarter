package at.fyayc.backend.auth

import at.fyayc.backend.emporixapi.SessionTokenStorage
import at.fyayc.emporixapi.auth.CustomerOAuthClient
import at.fyayc.emporixapi.auth.EmporixSessionToken
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping

@Controller("/login")
class LoginController(
    private val customerOAuthClient: CustomerOAuthClient,
    private val sessionTokenStorage: SessionTokenStorage,
) {
    @PostMapping(path = ["/sso"], consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun sso(credentials: EmporixSessionToken) {
//        val token = storage.getOrRefreshSessionToken()
//        val sessionAndUser = runBlocking(Dispatchers.Default) {
//            runOnVirtualThreadAndInterruptible {
//                Files.writeString(java.nio.file.Path())
//            }
//            val session = async { sessionClient.getCurrentSession(token) }
//            val user = async { userClient.getCurrentUser() }
//            return session.await() to user.await()
//        }
        // TODO: retrieve session and user info
        // TODO: store info in session and fire user logged in event
    }

    @PostMapping(path = ["/password"], consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun password(credentials: UsernamePasswordLogin) {
//        val result = future {
        // TODO: retrieve session info
//        }
        // TODO: store info in session and fire user logged in event
    }
}