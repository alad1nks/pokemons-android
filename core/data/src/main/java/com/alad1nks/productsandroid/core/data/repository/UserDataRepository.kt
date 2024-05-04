package com.alad1nks.productsandroid.core.data.repository

import com.alad1nks.productsandroid.core.model.UserData
import kotlinx.coroutines.flow.Flow

interface UserDataRepository {
    val userData: Flow<UserData>
}
