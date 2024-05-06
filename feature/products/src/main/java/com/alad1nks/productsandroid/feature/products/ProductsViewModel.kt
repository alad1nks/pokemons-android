package com.alad1nks.productsandroid.feature.products

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alad1nks.productsandroid.core.data.repository.ProductsRepository
import com.alad1nks.productsandroid.core.data.repository.UserDataRepository
import com.alad1nks.productsandroid.core.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val repository: ProductsRepository,
    private val userDataRepository: UserDataRepository
) : ViewModel() {
    private val disposables = CompositeDisposable()

    private val searchQuerySubject = PublishSubject.create<String>()

    private val _uiState: MutableLiveData<ProductsUiState> = MutableLiveData()
    val uiState: LiveData<ProductsUiState> = _uiState

    val darkTheme: Flow<Boolean> = userDataRepository.userData.map { it.darkTheme }

    init {
        disposables.add(
            searchQuerySubject
                .debounce(300, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .switchMapSingle { query ->
                    repository.getProducts()
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { items -> _uiState.value = ProductsUiState.Data(items) },
                    { _ -> _uiState.value = ProductsUiState.Error }
                )
        )
        searchQuerySubject.onNext("")
    }

    fun loadMore(skip: Int) {
        disposables.add(
            repository.getProducts(skip)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { items ->
                        val products = when (val state = uiState.value) {
                            is ProductsUiState.Data -> state.products + items
                            else -> items
                        }
                        _uiState.value = ProductsUiState.Data(products)
                    },
                    { _ -> _uiState.value = ProductsUiState.Error }
                )
        )
    }

    fun refresh() {
        _uiState.value = ProductsUiState.Loading
        disposables.add(
            repository.getProducts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { items ->
                        val products = when (val state = uiState.value) {
                            is ProductsUiState.Data -> state.products + items
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

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }
}

sealed interface ProductsUiState {
    data object Loading : ProductsUiState

    data class Data(
        val products: List<Product>
    ) : ProductsUiState

    data object Error : ProductsUiState
}
