package at.fyayc.emporixapi.util

import kotlin.enums.enumEntries

@Suppress("EnumEntryName")
enum class LanguageIso {
    de,
    en,
    en_US,
    de_DE,
    de_AT,
    de_CH;

    companion object {
        fun fromIso(isoCode: String) =
            enumEntries<LanguageIso>().find { it.name == isoCode }
    }
}