package at.fyayc.backend.security.auth

import at.fyayc.emporixapi.session.CurrencyIso
import at.fyayc.emporixapi.util.LanguageIso

data class LoginSuccess(
    val languageIso: LanguageIso,
    val currencyIso: CurrencyIso,
)