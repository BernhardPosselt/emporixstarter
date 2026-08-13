package at.fyayc.emporixapi.customer

import at.fyayc.emporixapi.auth.ServiceToken
import at.fyayc.emporixapi.http.ApiConfig
import at.fyayc.emporixapi.http.parseOptionalOrThrow
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*

class CustomerClient(
    val apiConfig: ApiConfig,
    val client: HttpClient,
) {
    // note: expand is not implemented
    suspend fun getProfile(
        customerNumber: String,
        serviceToken: ServiceToken,
        expand: List<String> = emptyList()
    ): TenantManagedCustomer? {
        return client.post(apiConfig.baseUrl) {
            url {
                appendPathSegments("customer", apiConfig.tenant, "customers", customerNumber)
                parameters {
                    append("expand", expand.joinToString(","))
                }
                bearerAuth(serviceToken.accessToken)
            }
            contentType(ContentType.Application.Json)
        }.parseOptionalOrThrow<TenantManagedCustomer>()
    }
}