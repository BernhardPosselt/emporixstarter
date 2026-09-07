package at.fyayc.emporixapi.iam

import at.fyayc.emporixapi.auth.token.ServiceToken
import at.fyayc.emporixapi.http.ApiConfig
import at.fyayc.emporixapi.http.parseOrThrow
import at.fyayc.emporixapi.i18n.AcceptLanguage
import at.fyayc.emporixapi.i18n.acceptLanguage
import at.fyayc.emporixapi.pagination.Pagination
import at.fyayc.emporixapi.pagination.paginateWith
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.flow.Flow

class IAMClient(
    private val client: HttpClient,
    private val apiConfig: ApiConfig,
) {
    fun getUserGroups(
        userId: String,
        serviceToken: ServiceToken,
        pagination: Pagination = Pagination(),
        language: AcceptLanguage = AcceptLanguage.all(),
    ): Flow<UserGroup> = pagination.paginate { currentPage ->
        client.get(apiConfig.baseUrl) {
            url {
                appendPathSegments("iam", apiConfig.tenant, "users", userId, "groups")
                parameters.paginateWith(currentPage)
            }
            headers {
                acceptLanguage(language)
            }
            bearerAuth(serviceToken.accessToken)
            contentType(ContentType.Application.Json)
        }.parseOrThrow<List<UserGroup>>()
    }
}