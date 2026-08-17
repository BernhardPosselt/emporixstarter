package at.fyayc.emporixapi.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow


data class Pagination(
    val pageNumber: Int = 1,
    val pageSize: Int = 60,
    val sort: List<SortParam> = emptyList(),
) {
    fun <T> paginate(
        fetchPage: suspend (CurrentPage) -> List<T>,
    ): Flow<T> {
        val sorted = sort
            .joinToString(",") { it.serialize() }
            .takeIf { it.isNotBlank() }
        val pageSize = pageSize
        return flow {
            var i = pageNumber
            var pageResult: List<T>
            do {
                pageResult = fetchPage(CurrentPage(i, pageSize, sorted))
                emitAll(pageResult.asFlow())
                i += 1
            } while (pageResult.isNotEmpty())
        }
    }
}