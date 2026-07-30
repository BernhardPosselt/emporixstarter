package at.fyayc.emporixapi.wrappers

import at.fyayc.emporixapi.Configuration
import at.fyayc.emporixapi.products.ProductClient

@JsExport
class ProductApi(
    config: ApiConfiguration,
    client: EmporixHttpClient,
) {
    private val client = ProductClient(
        Configuration(
            client = client.client,
            endpoint = config.baseUrl,
            tenant = config.tenant,
        )
    )

    private createProduct() {
        client.createProduct()
    }
}