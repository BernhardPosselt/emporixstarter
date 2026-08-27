package at.fyayc.emporixapi.util

import io.ktor.http.*

data class CurrentPage(
    val index: Int,
    val size: Int,
    val sort: String?,
)


fun ParametersBuilder.paginateWith(page: CurrentPage) {
    page.sort?.let { append("sort", it) }
    append("pageSize", page.size.toString())
    append("pageNumber", page.index.toString())
}