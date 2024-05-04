package com.alad1nks.productsandroid.core.data.repository

import com.alad1nks.productsandroid.core.model.DarkTheme
import com.alad1nks.productsandroid.core.model.UserData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class UserDataRepositoryImpl @Inject constructor() : UserDataRepository {
    override val userData: Flow<UserData>
        get() = flowOf(UserData(DarkTheme.SYSTEM, true))
}
