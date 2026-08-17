package at.fyayc.backend.auth

import at.fyayc.backend.emporixapi.ServiceTokenStorage
import at.fyayc.backend.emporixapi.SessionTokenStorage
import at.fyayc.emporixapi.auth.CustomerCredentials
import at.fyayc.emporixapi.auth.CustomerOAuthClient
import at.fyayc.emporixapi.auth.IAMClient
import at.fyayc.emporixapi.auth.token.LeasedCustomerToken
import at.fyayc.emporixapi.customer.CustomerClient
import at.fyayc.emporixapi.session.SessionClient
import at.fyayc.emporixapi.util.LanguageIso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.springframework.http.MediaType
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
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
    fun sso(credentials: LeasedCustomerToken) {
        runBlocking(Dispatchers.Default) {
            populateSession(credentials)
        }
    }

    @PostMapping(path = ["/password"], consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun password(credentials: PasswordLogin) {
        runBlocking(Dispatchers.Default) {
            // TODO: try catch exceptions from retrieving tokens and translate to http exception
            val leasedCustomerToken = customerOAuthClient.login(
                credentials = CustomerCredentials(
                    email = credentials.email,
                    password = credentials.password,
                )
            )
            populateSession(leasedCustomerToken)
        }
    }

    data class UserGroup(
        val id: String,
        val name: String,
    ) : GrantedAuthority {
        override fun getAuthority(): String = id
    }

    // TODO: this object needs to be serialized with jackson, so all values need to be serializable with jackson
    data class User(
        val userId: String,
        val groups: List<UserGroup>,
        val isActive: Boolean,
        val isOnHold: Boolean,
    ) : UserDetails {
        override fun getAuthorities(): Collection<GrantedAuthority> = groups

        override fun getPassword() = null

        override fun getUsername(): String = userId

        override fun isEnabled() = isActive && !isOnHold
    }

    private suspend fun populateSession(
        leasedCustomerToken: LeasedCustomerToken
    ) = coroutineScope {
        val leasedServiceToken = serviceTokenStorage.retrieve()
        val emporixSessionDeferred = async { sessionClient.ownSessionContext(leasedCustomerToken.token) }
        // stupid Emporix does not return login status and your own groups in this response
        // furthermore, we can't use the userId returned from emporixSession since Emporix
        // requires the customerNumber which needs to be looked up with the following call
        val myProfileDeferred = async { customerClient.getOwnProfile(leasedCustomerToken.token) }
        val myProfile = myProfileDeferred.await()
        val userInfoDeferred = async { customerClient.getProfile(myProfile.customerNumber, leasedServiceToken.token) }
        val userGroupsDeferred = async {
            iamClient.getUserGroups(myProfile.id, leasedServiceToken.token)
                .toList()
        }
        // TODO: proper exception: user does not exist; important: prevent account enumeration
        val userInfo = userInfoDeferred.await() ?: throw Exception()
        val userGroups = userGroupsDeferred.await()
        val emporixSession = emporixSessionDeferred.await()
        val user = User(
            groups = userGroups.map {
                UserGroup(
                    id = it.id,
                    // TODO: how do we determine the group name? do we want to store translated names as well? do we want a fallback?
                    name = it.name[LanguageIso.EN] ?: throw Exception()
                )
            },
            isOnHold = userInfo.onHold,
            isActive = userInfo.active,
            userId = userInfo.id,
        )
        // TODO: think about what we want to store how in a session; a value should be stored separately
        // emporixSession = emporixSession,
        // TODO: store user in session and deal with spring cookie and security crap
        sessionTokenStorage.store(leasedCustomerToken)
    }
}