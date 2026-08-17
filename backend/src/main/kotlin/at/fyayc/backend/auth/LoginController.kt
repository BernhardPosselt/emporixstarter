package at.fyayc.backend.auth

import at.fyayc.backend.emporixapi.ServiceTokenStorage
import at.fyayc.backend.emporixapi.SessionTokenStorage
import at.fyayc.emporixapi.auth.CustomerCredentials
import at.fyayc.emporixapi.auth.CustomerOAuthClient
import at.fyayc.emporixapi.auth.IAMClient
import at.fyayc.emporixapi.auth.token.CustomerToken
import at.fyayc.emporixapi.customer.CustomerClient
import at.fyayc.emporixapi.session.SessionClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping

@Controller("/login")
class LoginController(
    private val customerOAuthClient: CustomerOAuthClient,
    private val sessionClient: SessionClient,
    private val iamClient: IAMClient,
    private val customerClient: CustomerClient,
    private val sessionTokenStorage: SessionTokenStorage,
    private val serviceTokenStorage: ServiceTokenStorage,
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
            // TODO: try catch exceptions from retrieving tokens and translate to http exception
            val serviceTokenDeferred = async { serviceTokenStorage.retrieve() }
            val leasedTokenDeferred = async {
                customerOAuthClient.login(
                    credentials = CustomerCredentials(
                        email = credentials.email,
                        password = credentials.password,
                    )
                )
            }

            val leasedToken = leasedTokenDeferred.await()
            val emporixSession = async { sessionClient.ownSessionContext(leasedToken.token) }
            // stupid Emporix does not return login status and your own groups in this response
            // furthermore, we can't use the userId returned from emporixSession since Emporix
            // requires the customerNumber which needs to be looked up with the following call
            val myProfileDeferred = async { customerClient.getOwnProfile(leasedToken.token) }
            val serviceToken = serviceTokenDeferred.await()
            val myProfile = myProfileDeferred.await()
            val userInfo = customerClient.getProfile(myProfile.customerNumber, serviceToken.token)
            val userGroups = iamClient.getUserGroups(myProfile.id, serviceToken.token)
                .map { it.id }
                .toList()
            // TODO: store token in session and deal with spring cookie and security crap
        }
    }

    private suspend fun retrieveCustomerData() = coroutineScope {
        async {

        }
    }
}