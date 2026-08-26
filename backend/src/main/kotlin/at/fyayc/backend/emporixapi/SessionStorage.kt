package at.fyayc.backend.emporixapi

import at.fyayc.emporixapi.i18n.CountryIso
import at.fyayc.emporixapi.i18n.CurrencyIso
import at.fyayc.emporixapi.i18n.LanguageIso
import jakarta.servlet.http.HttpSession
import org.springframework.stereotype.Service

/**
 * IMPORTANT:
 *
 * Typed access to Spring's HttpSession interface. Spring Session has a concurrency mechanism built in
 * that tries to prevent lost updates. Whenever a value is changed, only that value is saved back
 * to the JSON session in redis. If you persist an entire object in a session value, that object
 * should be updated all at once. If you only update a single property on the persisted object,
 * this property might be overridden by another concurrent update, that updates another property on that object
 *
 * Furthermore, any type set in here must be serializable by Jackson
 */
@Service
class SessionStorage(
    httpSession: HttpSession,
) {
    var language by httpSession.property<LanguageIso?>("EMPORIX_LANGUAGE")
    var sessionId by httpSession.property<String?>("EMPORIX_ID")
    var customerId by httpSession.property<String?>("EMPORIX_CUSTOMER_ID")
    var siteCode by httpSession.property<String?>("EMPORIX_SITE_CODE")
    var currency by httpSession.property<CurrencyIso?>("EMPORIX_CURRENCY")
    var cartId by httpSession.property<String?>("EMPORIX_CART_ID")
    var targetLocation by httpSession.property<CountryIso?>("EMPORIX_TARGET_LOCATION")
}