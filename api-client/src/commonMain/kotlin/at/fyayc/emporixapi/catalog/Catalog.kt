package at.fyayc.emporixapi.catalog

import at.fyayc.emporixapi.i18n.TranslatedValue
import at.fyayc.emporixapi.util.Metadata

data class Catalog(
    val id: String,
    val name: TranslatedValue<String>,
    val description: TranslatedValue<String>,
    val visibility: VisibilityInformation?,
    val publishedSites: Set<String>,
    val categoryIds: Set<String>,
    val metadata: Metadata
)