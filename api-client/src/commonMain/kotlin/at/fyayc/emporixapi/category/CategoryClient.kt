package at.fyayc.emporixapi.category

import at.fyayc.emporixapi.auth.token.ServiceToken
import at.fyayc.emporixapi.http.ApiConfig
import at.fyayc.emporixapi.i18n.AcceptLanguage
import at.fyayc.emporixapi.i18n.acceptLanguage
import at.fyayc.emporixapi.pagination.PaginatedResult
import at.fyayc.emporixapi.pagination.Pagination
import at.fyayc.emporixapi.pagination.paginateWith
import at.fyayc.emporixapi.pagination.toPaginatedResult
import at.fyayc.emporixapi.pagination.totalCount
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*

class CategoryClient(
    val apiConfig: ApiConfig,
    val client: HttpClient,
) {
    suspend fun getCategories(
        serviceToken: ServiceToken,
        pagination: Pagination = Pagination(),
        filters: CategoryFilters? = null,
        language: AcceptLanguage = AcceptLanguage.all(),
    ): PaginatedResult<Category> = pagination.paginateWithTotalCount { currentPage ->
        client.get(apiConfig.baseUrl) {
            url {
                appendPathSegments("category", apiConfig.tenant, "categories")
                parameters.paginateWith(currentPage)
                filters?.let(parameters::addFilters)
            }
            headers {
                acceptLanguage(language)
                totalCount()
            }
            bearerAuth(serviceToken.accessToken)
            contentType(ContentType.Application.Json)
        }.toPaginatedResult()
    }
//
//    suspend fun getCatalogsByCategory(
//        categoryId: String,
//        serviceToken: ServiceToken,
//        pagination: Pagination = Pagination(),
//        language: AcceptLanguage = AcceptLanguage.all(),
//    ): PaginatedResult<Catalog> = pagination.paginateWithTotalCount { currentPage ->
//        client.get(apiConfig.baseUrl) {
//            url {
//                appendPathSegments("catalog", apiConfig.tenant, "catalogs", "categories", categoryId)
//                parameters.paginateWith(currentPage)
//            }
//            headers {
//                acceptLanguage(language)
//                totalCount()
//            }
//            bearerAuth(serviceToken.accessToken)
//            contentType(ContentType.Application.Json)
//        }.toPaginatedResult()
//    }
//
//    suspend fun getCatalogById(
//        id: String,
//        serviceToken: ServiceToken,
//        language: AcceptLanguage = AcceptLanguage.all(),
//    ): Catalog? =
//        client.get(apiConfig.baseUrl) {
//            url {
//                appendPathSegments("catalog", apiConfig.tenant, "catalogs", id)
//            }
//            headers {
//                acceptLanguage(language)
//            }
//            bearerAuth(serviceToken.accessToken)
//            contentType(ContentType.Application.Json)
//        }.parseOptionalOrThrow()
//
//    suspend fun upsertCatalog(
//        id: String,
//        catalog: CreateCatalog,
//        serviceToken: ServiceToken,
//    ): SaveResult<Catalog>? {
//        val response = client.put(apiConfig.baseUrl) {
//            url {
//                appendPathSegments("catalog", apiConfig.tenant, "catalogs", id)
//            }
//            setBody(catalog)
//            bearerAuth(serviceToken.accessToken)
//            contentType(ContentType.Application.Json)
//        }
//        return response.parseOptionalOrThrow<Catalog>()?.let {
//            SaveResult(
//                it,
//                mode = when (response.status.value) {
//                    201 -> SaveMode.CREATED
//                    204 -> SaveMode.UPDATED
//                    else -> throw RuntimeException("Could not determine save result from code ${response.status.value}")
//                }
//            )
//        }
//    }
//
//    suspend fun deleteCatalog(id: String, serviceToken: ServiceToken) {
//        client.delete(apiConfig.baseUrl) {
//            url {
//                appendPathSegments("catalog", apiConfig.tenant, "catalogs", id)
//            }
//            bearerAuth(serviceToken.accessToken)
//            contentType(ContentType.Application.Json)
//        }
//    }

    // TODO: https://developer.emporix.io/api-references/api-guides/catalogs-and-categories/catalog/api-reference/catalog-management#patch-catalog-tenant-catalogs-catalogid
}

