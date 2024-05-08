package com.alad1nks.productsandroid.feature.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alad1nks.productsandroid.core.data.repository.ProductRepository
import com.alad1nks.productsandroid.core.model.ProductInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {
    private val disposables = CompositeDisposable()

    private val _uiState: MutableLiveData<ProductUiState> = MutableLiveData()
    val uiState: LiveData<ProductUiState> = _uiState

    fun refresh(id: Int) {
        _uiState.value = ProductUiState.Loading
        disposables.add(
            repository.getProduct(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { product -> _uiState.value = ProductUiState.Data(product) },
                    { _ -> _uiState.value = ProductUiState.Error }
                )
        )
    }
}

sealed interface ProductUiState {
    data object Loading : ProductUiState

    data class Data(
        val product: ProductInfo
    ) : ProductUiState

    data object Error : ProductUiState
}
