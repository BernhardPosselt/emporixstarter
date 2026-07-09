package at.fyayc.emporixapi.products

import kotlin.js.JsExport

@JsExport
interface IProductClient {
    suspend fun createProduct(
        product: CreateProduct,
        token: String,
        skipVariantGeneration: Boolean = false,
        doIndex: Boolean = false,
        contentLanguage: String? = null,
    )
}