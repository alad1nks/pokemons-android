package com.alad1nks.productsandroid.core.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import com.alad1nks.productsandroid.core.datastore.PreferencesKeys.DARK_THEME
import com.alad1nks.productsandroid.core.model.UserData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

private const val USER_PREFERENCES_NAME = "app_datastore"

@Singleton
class LocalDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) : DataStore {

    private val Context.dataStore by preferencesDataStore(
        name = USER_PREFERENCES_NAME
    )

    override fun userPreferencesFlow(): Flow<UserData> =
        context.dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                val darkTheme = preferences[DARK_THEME] ?: false
                UserData(
                    darkTheme = darkTheme
                )
            }

    override suspend fun changeTheme() {
        context.dataStore.edit { preferences ->
            preferences[DARK_THEME] = preferences[DARK_THEME] == false
        }
    }
}
