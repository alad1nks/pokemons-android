package com.alad1nks.productsandroid.core.data.repository

import com.alad1nks.productsandroid.core.model.Pokemon
import io.reactivex.rxjava3.core.Single

interface ProductsRepository {
    fun getProducts(
        skip: Int = 0,
        limit: Int = 20
    ): Single<List<Pokemon>>
}
