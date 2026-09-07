package at.fyayc.emporixapi.pagination

import at.fyayc.emporixapi.http.parseOrThrow
import io.ktor.client.statement.*

data class Page<T>(
    val values: List<T>,
    val totalCount: Int,
)

suspend inline fun <reified T> HttpResponse.toPaginatedResult() =
    Page(
        values = parseOrThrow<List<T>>(),
        totalCount = headers["X-Total-Count"]?.toInt()
            ?: throw RuntimeException("No total count in response")
    )
