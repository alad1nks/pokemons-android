package com.alad1nks.productsandroid.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class ProductResponse(
    val id: Int,
    val title: String,
    val description: String,
    val price: Int,
    val discountPercentage: Float,
    val rating: Float,
    val stock: Int,
    val brand: String,
    val category: String,
    val thumbnail: String,
    val images: List<String>
)
