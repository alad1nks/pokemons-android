package com.alad1nks.productsandroid.core.network.retrofit

import com.alad1nks.productsandroid.core.network.NetworkDataSource
import com.alad1nks.productsandroid.core.network.model.PokemonInfoResponse
import com.alad1nks.productsandroid.core.network.model.PokemonsResponse
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import io.reactivex.rxjava3.core.Single
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

private interface RetrofitNetworkApi {
    @GET("pokemon")
    fun getProducts(
        @Query("skip") skip: Int,
        @Query("limit") limit: Int
    ): Single<PokemonsResponse>

    @GET("pokemon/{id}")
    fun getProduct(
        @Path("id") id: Int
    ): Single<PokemonInfoResponse>
}

private const val BASE_URL = "https://pokeapi.co/api/v2/"

@Singleton
class RetrofitNetwork @Inject constructor(
    networkJson: Json
) : NetworkDataSource {
    private val client = OkHttpClient.Builder().addInterceptor { chain ->
        val requestBuilder = chain.request().newBuilder()
        val response = chain.proceed(requestBuilder.build())
        if (response.code == 502) {
            throw IOException("Server is turned off")
        }
        return@addInterceptor response
    }.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)).build()

    private val networkApi = Retrofit.Builder()
        .client(client)
        .baseUrl(BASE_URL)
        .addConverterFactory(
            networkJson.asConverterFactory("application/json".toMediaType()),
        )
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()
        .create(RetrofitNetworkApi::class.java)

    override fun getPokemons(skip: Int, limit: Int): Single<PokemonsResponse> =
        networkApi.getProducts(skip, limit)

    override fun getPokemon(id: Int): Single<PokemonInfoResponse> =
        networkApi.getProduct(id)
}
