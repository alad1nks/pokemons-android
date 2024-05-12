package com.alad1nks.productsandroid.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class PokemonResponse(
    val name: String?,
    val url: String?
)
