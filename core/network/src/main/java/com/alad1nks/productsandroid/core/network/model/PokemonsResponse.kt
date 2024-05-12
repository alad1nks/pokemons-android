package com.alad1nks.productsandroid.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class PokemonsResponse(
    val count: Int?,
    val next: String?,
    val previous: String?,
    val results: List<PokemonResponse>?
)
