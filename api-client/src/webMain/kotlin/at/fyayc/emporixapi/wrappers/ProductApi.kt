package at.fyayc.emporixapi.wrappers

import at.fyayc.emporixapi.Configuration
import at.fyayc.emporixapi.products.IProductClient
import at.fyayc.emporixapi.products.ProductClient

@JsExport
class ProductApi(
    private val config: ApiConfiguration,
    private val client: EmporixHttpClient,
) : IProductClient by ProductClient(
    Configuration(
        client = client.client,
        endpoint = config.baseUrl,
        tenant = config.tenant,
    )
)