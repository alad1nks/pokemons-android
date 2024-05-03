package com.alad1nks.productsandroid

import androidx.lifecycle.ViewModel
import com.alad1nks.productsandroid.core.data.repository.UserDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    repository: UserDataRepository
) : ViewModel() {
    val darkTheme: Flow<Boolean> = repository.darkTheme
}
