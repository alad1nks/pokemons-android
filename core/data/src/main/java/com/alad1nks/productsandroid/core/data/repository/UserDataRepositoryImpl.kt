package com.alad1nks.productsandroid.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class UserDataRepositoryImpl @Inject constructor() : UserDataRepository {
    override val darkTheme: Flow<Boolean>
        get() = flowOf(false)
}
