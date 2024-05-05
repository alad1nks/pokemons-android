package com.alad1nks.productsandroid.core.data.repository

import com.alad1nks.productsandroid.core.datastore.DataStore
import com.alad1nks.productsandroid.core.model.UserData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserDataRepositoryImpl @Inject constructor(
    private val dataStore: DataStore
) : UserDataRepository {
    override val userData: Flow<UserData>
        get() = dataStore.userPreferencesFlow()

    override suspend fun changeTheme() {
        dataStore.changeTheme()
    }
}
