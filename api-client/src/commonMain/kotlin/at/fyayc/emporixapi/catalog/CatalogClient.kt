package at.fyayc.emporixapi.catalog

import at.fyayc.emporixapi.auth.token.ServiceToken
import at.fyayc.emporixapi.http.ApiConfig
import at.fyayc.emporixapi.http.parseOptionalOrThrow
import at.fyayc.emporixapi.i18n.AcceptLanguage
import at.fyayc.emporixapi.i18n.acceptLanguage
import at.fyayc.emporixapi.pagination.PaginatedResult
import at.fyayc.emporixapi.pagination.Pagination
import at.fyayc.emporixapi.pagination.paginateWith
import at.fyayc.emporixapi.pagination.toPaginatedResult
import at.fyayc.emporixapi.pagination.totalCount
import at.fyayc.emporixapi.util.SaveMode
import at.fyayc.emporixapi.util.SaveResult
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*

class CatalogClient(
    val apiConfig: ApiConfig,
    val client: HttpClient,
) {
    suspend fun getCatalogs(
        serviceToken: ServiceToken,
        pagination: Pagination = Pagination(),
        filters: CatalogFilters? = null,
        language: AcceptLanguage = AcceptLanguage.all(),
    ): PaginatedResult<Catalog> = pagination.paginateWithTotalCount { currentPage ->
        client.get(apiConfig.baseUrl) {
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
                totalCount()
            }
            bearerAuth(serviceToken.accessToken)
            contentType(ContentType.Application.Json)
        }.toPaginatedResult()
    }

    suspend fun getCatalogsByCategory(
        categoryId: String,
        serviceToken: ServiceToken,
        pagination: Pagination = Pagination(),
        language: AcceptLanguage = AcceptLanguage.all(),
    ): PaginatedResult<Catalog> = pagination.paginateWithTotalCount { currentPage ->
        client.get(apiConfig.baseUrl) {
            url {
                appendPathSegments("catalog", apiConfig.tenant, "catalogs", "categories", categoryId)
                parameters.paginateWith(currentPage)
            }
            headers {
                acceptLanguage(language)
                totalCount()
            }
            bearerAuth(serviceToken.accessToken)
            contentType(ContentType.Application.Json)
        }.toPaginatedResult()
    }

    suspend fun getCatalogById(
        id: String,
        serviceToken: ServiceToken,
        language: AcceptLanguage = AcceptLanguage.all(),
    ): Catalog? =
        client.get(apiConfig.baseUrl) {
            url {
                appendPathSegments("catalog", apiConfig.tenant, "catalogs", id)
            }
            headers {
                acceptLanguage(language)
            }
            bearerAuth(serviceToken.accessToken)
            contentType(ContentType.Application.Json)
        }.parseOptionalOrThrow()

    suspend fun upsertCatalog(
        id: String,
        catalog: CreateCatalog,
        serviceToken: ServiceToken,
    ): SaveResult<Catalog>? {
        val response = client.put(apiConfig.baseUrl) {
            url {
                appendPathSegments("catalog", apiConfig.tenant, "catalogs", id)
            }
            setBody(catalog)
            bearerAuth(serviceToken.accessToken)
            contentType(ContentType.Application.Json)
        }
        return response.parseOptionalOrThrow<Catalog>()?.let {
            SaveResult(
                it,
                mode = when (response.status.value) {
                    201 -> SaveMode.CREATED
                    204 -> SaveMode.UPDATED
                    else -> throw RuntimeException("Could not determine save result from code ${response.status.value}")
                }
            )
        }
    }

    suspend fun deleteCatalog(id: String, serviceToken: ServiceToken) {
        client.delete(apiConfig.baseUrl) {
            url {
                appendPathSegments("catalog", apiConfig.tenant, "catalogs", id)
            }
            bearerAuth(serviceToken.accessToken)
            contentType(ContentType.Application.Json)
        }
    }

    // TODO: https://developer.emporix.io/api-references/api-guides/catalogs-and-categories/catalog/api-reference/catalog-management#patch-catalog-tenant-catalogs-catalogid
}

