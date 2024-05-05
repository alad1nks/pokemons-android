package com.alad1nks.productsandroid.core.datastore.di

import com.alad1nks.productsandroid.core.datastore.DataStore
import com.alad1nks.productsandroid.core.datastore.LocalDataStore
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataStoreModule {
    @Singleton
    @Binds
    abstract fun bindStorage(storage: LocalDataStore): DataStore
}
