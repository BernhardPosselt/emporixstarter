package at.fyayc.batteryincludedindexer

import kotlinx.serialization.Serializable

@Serializable
data class SapArticle(
    val id: String,
)

@Serializable
data class IndexedProduct(
    val id: String,
    val cooperative: Map<String, Cooperative>,
    val name: String,
)

@Serializable
data class Cooperative(
    val price: String,
)