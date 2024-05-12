package com.alad1nks.productsandroid.core.data.repository

import com.alad1nks.productsandroid.core.model.Pokemon
import com.alad1nks.productsandroid.core.network.NetworkDataSource
import com.alad1nks.productsandroid.core.network.model.PokemonsResponse
import io.reactivex.rxjava3.core.Single
import java.io.IOException
import javax.inject.Inject

class ProductsRepositoryImpl @Inject constructor(
    private val dataSource: NetworkDataSource
) : ProductsRepository {
    override fun getProducts(
        skip: Int,
        limit: Int
    ): Single<List<Pokemon>> {
        return dataSource.getPokemons(skip, limit)
            .map { response -> response.toModel() }
            .onErrorResumeNext { throwable: Throwable ->
                if (throwable is IOException) {
                    Single.error(Exception("Network error occurred"))
                } else {
                    Single.error(throwable)
                }
            }
    }

    private fun PokemonsResponse.toModel(): List<Pokemon> {
        return results?.map { pokemon ->
            Pokemon(pokemon.name ?: "")
        } ?: emptyList()
    }
}
