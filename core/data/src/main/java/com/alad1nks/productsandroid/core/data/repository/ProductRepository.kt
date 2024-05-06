package com.alad1nks.productsandroid.core.data.repository

import com.alad1nks.productsandroid.core.model.ProductInfo
import io.reactivex.rxjava3.core.Single

interface ProductRepository {
    fun getProduct(id: Int): Single<ProductInfo>
}
