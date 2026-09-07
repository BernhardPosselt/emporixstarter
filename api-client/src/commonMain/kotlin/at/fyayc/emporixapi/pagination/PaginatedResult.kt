package at.fyayc.emporixapi.pagination

import io.ktor.http.*
import kotlinx.coroutines.flow.Flow

data class PaginatedResult<T>(
    val values: Flow<T>,
    val totalCount: Int,
)

fun HeadersBuilder.totalCount(add: Boolean = true) {
    append("X-Total-Count", add.toString())
}