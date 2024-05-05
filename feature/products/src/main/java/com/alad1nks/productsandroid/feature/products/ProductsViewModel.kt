package com.alad1nks.productsandroid.feature.products

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alad1nks.productsandroid.core.data.repository.ProductsRepository
import com.alad1nks.productsandroid.core.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val repository: ProductsRepository
) : ViewModel() {
    private val disposables = CompositeDisposable()

    private val searchQuerySubject = PublishSubject.create<String>()

    private val _uiState: MutableLiveData<ProductsUiState> = MutableLiveData()
    val uiState: LiveData<ProductsUiState> = _uiState

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
                    { error -> _uiState.value = ProductsUiState.Error }
                )
        )
        searchQuerySubject.onNext("")
    }


    fun updateSearchQuery(query: String) {
        searchQuerySubject.onNext(query)
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
