package com.alad1nks.productsandroid.core.data.repository

import kotlinx.coroutines.flow.Flow

interface UserDataRepository {
    val darkTheme: Flow<Boolean>
}
