package at.fyayc.backend.security.auth

import at.fyayc.emporixapi.i18n.CurrencyIso
import at.fyayc.emporixapi.i18n.LanguageIso

data class LoginSuccess(
    val languageIso: LanguageIso,
    val currencyIso: CurrencyIso,
)