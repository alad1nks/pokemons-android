package com.alad1nks.productsandroid.core.network

import com.alad1nks.productsandroid.core.network.model.ProductsResponse

interface NetworkDataSource {
    suspend fun getProducts(skip: Int, limit: Int): ProductsResponse
}
