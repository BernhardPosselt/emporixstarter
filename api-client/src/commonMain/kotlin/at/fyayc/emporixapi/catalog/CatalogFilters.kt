package at.fyayc.emporixapi.catalog

import at.fyayc.emporixapi.pagination.LocalizedStringFilter
import kotlinx.datetime.LocalDate

data class CatalogFilters(
    val name: LocalizedStringFilter? = null,
    val description: LocalizedStringFilter? = null,
    val publishedSite: String? = null,
    val visibilityFrom: LocalDate? = null,
    val visibilityTo: LocalDate? = null,
    val metadataUpdatedAt: LocalDate? = null,
)