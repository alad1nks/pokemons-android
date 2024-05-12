package com.alad1nks.productsandroid.feature.products

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alad1nks.productsandroid.core.data.repository.ProductsRepository
import com.alad1nks.productsandroid.core.data.repository.UserDataRepository
import com.alad1nks.productsandroid.core.model.Pokemon
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val repository: ProductsRepository,
    private val userDataRepository: UserDataRepository
) : ViewModel() {
    private val disposables = CompositeDisposable()

    private val _uiState: MutableLiveData<ProductsUiState> = MutableLiveData()
    val uiState: LiveData<ProductsUiState> = _uiState

    private val _shouldEndRefresh = MutableLiveData(false)
    val shouldEndRefresh: LiveData<Boolean> get() = _shouldEndRefresh

    val darkTheme: Flow<Boolean> = userDataRepository.userData.map { it.darkTheme }

    init {
        refresh()
    }

    fun refresh(swipe: Boolean = false) {
        _uiState.value = ProductsUiState.Loading
        disposables.add(
            repository.getProducts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { items ->
                        val products = when (val state = uiState.value) {
                            is ProductsUiState.Data -> state.pokemons + items
                            else -> items
                        }
                        _uiState.value = ProductsUiState.Data(products)
                        if (swipe) { _shouldEndRefresh.value = true }
                    },
                    { _ ->
                        _uiState.value = ProductsUiState.Error
                        if (swipe) { _shouldEndRefresh.value = true }
                    }
                )
        )
    }

    fun loadMore(skip: Int) {
        disposables.add(
            repository.getProducts(skip)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { items ->
                        val products = when (val state = uiState.value) {
                            is ProductsUiState.Data -> state.pokemons + items
                            else -> items
                        }
                        _uiState.value = ProductsUiState.Data(products)
                    },
                    { _ -> _uiState.value = ProductsUiState.Error }
                )
        )
    }

    fun changeTheme() {
        viewModelScope.launch {
            userDataRepository.changeTheme()
        }
    }

    fun onRefreshEnded() {
        _shouldEndRefresh.value = false
    }

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }
}

sealed interface ProductsUiState {
    data object Loading : ProductsUiState

    data class Data(
        val pokemons: List<Pokemon>
    ) : ProductsUiState

    data object Error : ProductsUiState
}
