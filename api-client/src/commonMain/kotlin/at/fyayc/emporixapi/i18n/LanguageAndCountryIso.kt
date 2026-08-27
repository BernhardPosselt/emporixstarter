package at.fyayc.emporixapi.i18n

import kotlin.enums.enumEntries


@Suppress("EnumEntryName")
enum class LanguageAndCountryIso(
    val country: CountryIso,
    val languageIso: LanguageIso,
) {
    de_DE(CountryIso.DE, LanguageIso.DE),
    en_EN(CountryIso.EN, LanguageIso.EN),
    en_US(CountryIso.US, LanguageIso.EN);

    companion object {
        fun fromIso(isoCode: String) =
            enumEntries<LanguageAndCountryIso>().find { it.name == isoCode }
    }
}