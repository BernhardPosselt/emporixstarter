package at.fyayc.backend.security.auth

import at.fyayc.backend.emporixapi.ServiceTokenStorage
import at.fyayc.emporixapi.auth.CustomerCredentials
import at.fyayc.emporixapi.auth.CustomerOAuthClient
import at.fyayc.emporixapi.auth.IAMClient
import at.fyayc.emporixapi.auth.token.AnonymousToken
import at.fyayc.emporixapi.auth.token.CustomerToken
import at.fyayc.emporixapi.auth.token.LeasedCustomerToken
import at.fyayc.emporixapi.customer.CustomerClient
import at.fyayc.emporixapi.http.ApiError
import at.fyayc.emporixapi.i18n.LanguageKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service

@Service
class EmporixLoginService(
    private val customerOAuthClient: CustomerOAuthClient,
    private val iamClient: IAMClient,
    private val customerClient: CustomerClient,
    private val serviceTokenStorage: ServiceTokenStorage,
) {
    fun login(email: String, password: String, anonymousToken: AnonymousToken): Pair<User, LeasedCustomerToken> {
        return runBlocking(Dispatchers.Default) {
            try {
                val leasedCustomerToken = customerOAuthClient.login(
                    credentials = CustomerCredentials(
                        email = email,
                        password = password,
                    ),
                    anonymousToken
                )
                val cartMergeDeferred = async {
                    mergeCarts(
                        anonymousToken = anonymousToken,
                        leasedCustomerToken = leasedCustomerToken.token,
                    )
                }
                val userDeferred = async { retrieveUser(leasedCustomerToken) to leasedCustomerToken }
                cartMergeDeferred.await()
                userDeferred.await()
            } catch (e: ApiError) {
                throw EmporixLoginFailedException("Invalid credentials", e)
            }
        }
    }

    fun login(token: LeasedCustomerToken): Pair<User, LeasedCustomerToken> = runBlocking(Dispatchers.Default) {
        retrieveUser(token) to token
    }

    private suspend fun mergeCarts(
        anonymousToken: AnonymousToken,
        leasedCustomerToken: CustomerToken,
    ) {
        // TODO
    }

    private suspend fun retrieveUser(
        leasedCustomerToken: LeasedCustomerToken
    ): User = coroutineScope {
        val leasedServiceToken = serviceTokenStorage.retrieve()
        // Emporix does not return login status and your own groups in this response
        // furthermore, we can't use the userId returned from emporixSession since Emporix
        // requires the customerNumber which needs to be looked up with the following call
        val myProfileDeferred = async { customerClient.getOwnProfile(leasedCustomerToken.token) }
        val myProfile = myProfileDeferred.await()
        val userInfoDeferred = async { customerClient.getProfile(myProfile.customerNumber, leasedServiceToken.token) }
        val userGroupsDeferred = async {
            iamClient.getUserGroups(myProfile.id, leasedServiceToken.token)
                .toList()
        }
        val userInfo = userInfoDeferred.await() ?: throw EmporixLoginFailedException("No Customer Found")
        val userGroups = userGroupsDeferred.await()
        User(
            groups = userGroups.map {
                UserGroup(
                    id = "ROLE_${it.id}",
                    // TODO: how do we determine the group name? do we want to store translated names as well? do we want a fallback?
                    name = it.name[LanguageKey.en] ?: throw Exception()
                )
            },
            isOnHold = userInfo.onHold,
            isActive = userInfo.active,
            userId = userInfo.id,
        )
    }
}