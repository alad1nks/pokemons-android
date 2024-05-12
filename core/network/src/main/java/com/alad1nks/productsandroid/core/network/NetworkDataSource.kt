package com.alad1nks.productsandroid.core.network

import com.alad1nks.productsandroid.core.network.model.PokemonInfoResponse
import com.alad1nks.productsandroid.core.network.model.PokemonsResponse
import io.reactivex.rxjava3.core.Single

interface NetworkDataSource {
    fun getPokemons(skip: Int, limit: Int): Single<PokemonsResponse>
    fun getPokemon(id: Int): Single<PokemonInfoResponse>
}
