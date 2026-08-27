package at.fyayc.backend.security.auth

import at.fyayc.backend.emporixapi.SessionStorage
import at.fyayc.backend.emporixapi.SessionTokenStorage
import at.fyayc.emporixapi.i18n.CurrencyIso
import at.fyayc.emporixapi.i18n.LanguageIso
import at.fyayc.emporixapi.session.SessionClient
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToStream
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component

@Component
class CustomerAuthenticationSuccessHandler(
    private val sessionClient: SessionClient,
    private val sessionStorage: SessionStorage,
    private val sessionTokenStorage: SessionTokenStorage,
    private val json: Json,
) : AuthenticationSuccessHandler {
    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        // TODO: check if user receives a new session cookie
        if (authentication is CustomerAuthenticationToken) {
            val leasedCustomerToken = authentication.token
            val emporixSession = runBlocking {
                sessionClient.ownSessionContext(leasedCustomerToken.token)
            }
            // TODO: do we need to clear these session values?
            sessionStorage.language = emporixSession.language
            sessionStorage.sessionId = emporixSession.sessionId
            sessionStorage.customerId = emporixSession.customerId
            sessionStorage.siteCode = emporixSession.siteCode?.name
            sessionStorage.currency = emporixSession.currency
            sessionStorage.cartId = emporixSession.cartId
            sessionStorage.targetLocation = emporixSession.targetLocation
            // TODO: custom session attributes are not persisted
            sessionTokenStorage.store(leasedCustomerToken)
            val requestLocale = request.locale
                ?.let {
                    LanguageIso.fromIso(it.language.uppercase())
                }
                ?: LanguageIso.EN
            response.status = HttpStatus.OK.value()
            // note: this is correct. we must not close the stream or additional headers set after this
            // call like the session cookie will not be written
            json.encodeToStream(
                LoginSuccess(
                    languageIso = emporixSession.language ?: requestLocale,
                    currencyIso = emporixSession.currency ?: CurrencyIso.EUR
                ),
                response.outputStream
            )
        }
    }
}