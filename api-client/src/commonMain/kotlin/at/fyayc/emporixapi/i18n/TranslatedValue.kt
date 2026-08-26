package at.fyayc.emporixapi.i18n

import kotlin.enums.enumEntries

typealias TranslatedValue<T> = Map<LanguageKey, T>

/**
 * Emporix has this annoying habit of uppercasing language iso codes but
 * lowercasing translation keys
 */
@Suppress("EnumEntryName")
enum class LanguageKey {
    de,
    en;

    companion object {
        fun fromIso(isoCode: String) =
            enumEntries<LanguageKey>().find { it.name.uppercase() == isoCode }

        fun fromLanguage(languageIso: LanguageIso) = when (languageIso) {
            LanguageIso.DE -> de
            LanguageIso.EN -> en
        }
    }
}