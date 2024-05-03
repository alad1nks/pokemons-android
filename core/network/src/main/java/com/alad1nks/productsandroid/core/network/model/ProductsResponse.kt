package com.alad1nks.productsandroid.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class ProductsResponse(
    val products: List<ProductResponse>
)
