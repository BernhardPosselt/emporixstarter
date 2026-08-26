package at.fyayc.emporixapi.i18n

import kotlin.enums.enumEntries

enum class LanguageIso {
    DE,
    EN;

    companion object {
        fun fromIso(isoCode: String) =
            enumEntries<LanguageIso>().find { it.name == isoCode }
    }
}