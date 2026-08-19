package at.fyayc.emporixapi.customer

import at.fyayc.emporixapi.auth.token.CustomerToken
import at.fyayc.emporixapi.auth.token.ServiceToken
import at.fyayc.emporixapi.http.ApiConfig
import at.fyayc.emporixapi.http.parseOptionalOrThrow
import at.fyayc.emporixapi.http.parseOrThrow
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*

class CustomerClient(
    val apiConfig: ApiConfig,
    val client: HttpClient,
) {
    // note: expand is not implemented
    suspend fun getOwnProfile(
        serviceToken: CustomerToken, expand: List<String> = emptyList()
    ): OwnCustomer {
        return client.post(apiConfig.baseUrl) {
            url {
                appendPathSegments("customer", apiConfig.tenant, "me")
                parameters.append("expand", expand.joinToString(","))
                bearerAuth(serviceToken.accessToken)
            }
            contentType(ContentType.Application.Json)
        }.parseOrThrow<OwnCustomer>()
    }

    // note: expand is not implemented
    suspend fun getProfile(
        customerNumber: String, serviceToken: ServiceToken, expand: List<String> = emptyList()
    ): TenantManagedCustomer? {
        return client.post(apiConfig.baseUrl) {
            url {
                appendPathSegments("customer", apiConfig.tenant, "customers", customerNumber)
                parameters.append("expand", expand.joinToString(","))
                bearerAuth(serviceToken.accessToken)
            }
            contentType(ContentType.Application.Json)
        }.parseOptionalOrThrow<TenantManagedCustomer>()
    }
}