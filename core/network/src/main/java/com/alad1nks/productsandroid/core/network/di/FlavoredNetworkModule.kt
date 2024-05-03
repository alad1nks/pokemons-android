package com.alad1nks.productsandroid.core.network.di

import com.alad1nks.productsandroid.core.network.NetworkDataSource
import com.alad1nks.productsandroid.core.network.retrofit.RetrofitNetwork
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface FlavoredNetworkModule {
    @Binds
    fun binds(impl: RetrofitNetwork): NetworkDataSource
}
