package com.alad1nks.productsandroid.core.data.repository

import com.alad1nks.productsandroid.core.model.PokemonInfo
import com.alad1nks.productsandroid.core.network.NetworkDataSource
import com.alad1nks.productsandroid.core.network.model.PokemonInfoResponse
import io.reactivex.rxjava3.core.Single
import java.io.IOException
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val dataSource: NetworkDataSource
) : ProductRepository {
    override fun getProduct(id: Int): Single<PokemonInfo> {
        return dataSource.getPokemon(id)
            .map { response -> response.toModel() }
            .onErrorResumeNext { throwable: Throwable ->
                if (throwable is IOException) {
                    Single.error(Exception("Network error occurred"))
                } else {
                    Single.error(throwable)
                }
            }
    }

    private fun PokemonInfoResponse.toModel(): PokemonInfo {
        return PokemonInfo(
            name = name,
            sprites = with(sprites) {
                listOf(frontDefault, frontShiny, backDefault, backShiny)
            },
            weight = weight.toString(),
            height = height.toString()
        )
    }
}
