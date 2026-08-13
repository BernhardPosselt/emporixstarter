package at.fyayc.backend.auth

import at.fyayc.backend.emporixapi.SessionTokenStorage
import at.fyayc.emporixapi.auth.CustomerCredentials
import at.fyayc.emporixapi.auth.CustomerOAuthClient
import at.fyayc.emporixapi.auth.CustomerToken
import at.fyayc.emporixapi.customer.CustomerClient
import at.fyayc.emporixapi.session.SessionClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping

@Controller("/login")
class LoginController(
    private val customerOAuthClient: CustomerOAuthClient,
    private val sessionClient: SessionClient,
//    private val iamClient: IAMClient,
    private val customerClient: CustomerClient,
    private val sessionTokenStorage: SessionTokenStorage,
) {
    @PostMapping(path = ["/sso"], consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun sso(credentials: CustomerToken) {
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
    fun password(credentials: PasswordLogin) {
        runBlocking(Dispatchers.Default) {
            // TODO: try catch exception and translate to http exception
            val user = customerOAuthClient.login(
                credentials = CustomerCredentials(
                    email = credentials.email,
                    password = credentials.password,
                )
            )
            val session = sessionClient.ownSessionContext(user.token)
        }
//        val result = future {
        // TODO: retrieve session info
//        }
        // TODO: store info in session and fire user logged in event
    }

    private suspend fun retrieveCustomerData() = coroutineScope {
        async {

        }
    }
}