package com.alad1nks.productsandroid.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class PokemonInfoResponse(
    val name: String,
    val sprites: SpritesResponse,
    val weight: String,
    val height: String
)
