package com.alad1nks.productsandroid.core.data.di

import com.alad1nks.productsandroid.core.data.repository.ProductRepository
import com.alad1nks.productsandroid.core.data.repository.ProductRepositoryImpl
import com.alad1nks.productsandroid.core.data.repository.ProductsRepository
import com.alad1nks.productsandroid.core.data.repository.ProductsRepositoryImpl
import com.alad1nks.productsandroid.core.data.repository.UserDataRepository
import com.alad1nks.productsandroid.core.data.repository.UserDataRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface DataModule {
    @Singleton
    @Binds
    fun bindProductRepository(repository: ProductRepositoryImpl): ProductRepository

    @Singleton
    @Binds
    fun bindProductsRepository(repository: ProductsRepositoryImpl): ProductsRepository

    @Singleton
    @Binds
    fun bindUserDataRepository(repository: UserDataRepositoryImpl): UserDataRepository
}
