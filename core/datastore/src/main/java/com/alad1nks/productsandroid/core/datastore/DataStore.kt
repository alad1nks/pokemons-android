package com.alad1nks.productsandroid.core.datastore

import com.alad1nks.productsandroid.core.model.UserData
import kotlinx.coroutines.flow.Flow

interface DataStore {
    fun userPreferencesFlow(): Flow<UserData>

    suspend fun changeTheme()
}
