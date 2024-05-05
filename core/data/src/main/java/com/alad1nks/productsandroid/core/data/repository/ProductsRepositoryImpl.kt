package com.alad1nks.productsandroid.core.data.repository

import com.alad1nks.productsandroid.core.model.Product
import com.alad1nks.productsandroid.core.network.NetworkDataSource
import com.alad1nks.productsandroid.core.network.model.ProductsResponse
import io.reactivex.rxjava3.core.Single
import java.io.IOException
import javax.inject.Inject

class ProductsRepositoryImpl @Inject constructor(
    private val dataSource: NetworkDataSource
) : ProductsRepository {
    override fun getProducts(
        skip: Int
    ): Single<List<Product>> {
        return dataSource.getProducts(skip)
            .map { response -> response.toModel() }
            .onErrorResumeNext { throwable: Throwable ->
                if (throwable is IOException) {
                    Single.error(Exception("Network error occurred"))
                } else {
                    Single.error(throwable)
                }
            }
    }

    private fun ProductsResponse.toModel(): List<Product> {
        return products.map { product ->
            with(product) {
                Product(id, title, description, price, thumbnail)
            }
        }
    }
}
