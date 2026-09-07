package at.fyayc.emporixapi.catalog

import at.fyayc.emporixapi.auth.token.ServiceToken
import at.fyayc.emporixapi.http.ApiConfig
import at.fyayc.emporixapi.http.parseOrThrow
import at.fyayc.emporixapi.i18n.AcceptLanguage
import at.fyayc.emporixapi.i18n.LanguageKey
import at.fyayc.emporixapi.pagination.Page
import at.fyayc.emporixapi.pagination.PaginatedResult
import at.fyayc.emporixapi.pagination.Pagination
import at.fyayc.emporixapi.pagination.paginateWith
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.datetime.LocalDate

data class Catalog(val id: String)

data class LocalizedStringFilter(
    val value: String,
    val locale: LanguageKey? = null,
) {
    val key = value + locale?.let { ".${it.name}" }
}

data class CatalogFilters(
    val name: LocalizedStringFilter? = null,
    val description: LocalizedStringFilter? = null,
    val publishedSite: String? = null,
    val visibilityFrom: LocalDate? = null,
    val visibilityTo: LocalDate? = null,
    val metadataUpdatedAt: LocalDate? = null,
)

fun HeadersBuilder.acceptLanguage(language: AcceptLanguage) {
    append("Accept-Language", language.string)
}

class CatalogClient(
    val apiConfig: ApiConfig,
    val client: HttpClient,
) {
    suspend fun getCatalogs(
        serviceToken: ServiceToken,
        pagination: Pagination = Pagination(),
        filters: CatalogFilters? = null,
        language: AcceptLanguage = AcceptLanguage.all()
    ): PaginatedResult<Catalog> = pagination.paginateWithTotalCount { currentPage ->
        val result = client.get(apiConfig.baseUrl) {
            url {
                appendPathSegments("catalog", apiConfig.tenant, "catalogs")
                parameters.paginateWith(currentPage)
                filters?.name?.let { parameters.append(it.key, it.value) }
                filters?.description?.let { parameters.append(it.key, it.value) }
                filters?.metadataUpdatedAt?.let { parameters.append("metadataUpdatedAt", it.toString()) }
                filters?.visibilityFrom?.let { parameters.append("visibilityFrom", it.toString()) }
                filters?.visibilityTo?.let { parameters.append("visibilityTo", it.toString()) }
                filters?.publishedSite?.let { parameters.append("publishedSite", it) }
            }
            headers {
                acceptLanguage(language)
                append("X-Total-Count", "true")
            }
            bearerAuth(serviceToken.accessToken)
            contentType(ContentType.Application.Json)
        }
        Page(
            values = result.parseOrThrow<List<Catalog>>(),
            totalCount = result.headers["X-Total-Count"]?.toInt()
                ?: throw RuntimeException("No total count in response")
        )
    }
}