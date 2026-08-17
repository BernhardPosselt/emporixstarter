package at.fyayc.emporixapi.auth

import at.fyayc.emporixapi.auth.token.ServiceToken
import at.fyayc.emporixapi.http.ApiConfig
import at.fyayc.emporixapi.http.parseOrThrow
import at.fyayc.emporixapi.util.Pagination
import at.fyayc.emporixapi.util.paginateWith
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
    ): Flow<UserGroup> = pagination.paginate { currentPage ->
        client.get(apiConfig.baseUrl) {
            url {
                appendPathSegments("iam", apiConfig.tenant, userId, "groups")
            }
            parameters {
                paginateWith(currentPage)
            }
            bearerAuth(serviceToken.accessToken)
            contentType(ContentType.Application.Json)
        }.parseOrThrow<List<UserGroup>>()
    }
}

