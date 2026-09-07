package at.fyayc.emporixapi.category

import at.fyayc.emporixapi.query.Query
import io.ktor.http.*
import kotlinx.datetime.LocalDate

data class CategoryFilters(
    val showRoots: Boolean = false,
    val showUnpublished: Boolean = false,
    val code: String? = null,
    val q: Query? = null,
    val localizedSlug: String? = null,
    val localizedName: String? = null,
    val localizedDescription: String? = null,
    val ecn: String? = null,
    val visibilityFrom: LocalDate? = null,
    val visibilityTo: LocalDate? = null,
    val metadataUpdatedAt: LocalDate? = null,
)

fun ParametersBuilder.addFilters(filters: CategoryFilters) {
    append("showRoots", filters.showRoots.toString())
    append("showUnpublished", filters.showUnpublished.toString())
    filters.code?.let { append("code", it) }
    filters.q?.let { append("q", it.toString()) }
    filters.localizedSlug?.let { append("localizedSlug", it) }
    filters.localizedName?.let { append("localizedName", it) }
    filters.localizedDescription?.let { append("localizedDescription", it) }
    filters.ecn?.let { append("ecn", it) }
    filters.visibilityFrom?.let { append("visibilityFrom", it.toString()) }
    filters.visibilityTo?.let { append("visibilityTo", it.toString()) }
    filters.metadataUpdatedAt?.let { append("metadataUpdatedAt", it.toString()) }
}