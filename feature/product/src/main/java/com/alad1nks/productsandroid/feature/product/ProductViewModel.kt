package com.alad1nks.productsandroid.feature.product

import androidx.lifecycle.ViewModel
import com.alad1nks.productsandroid.core.model.ProductInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor() : ViewModel() {
    val uiState: StateFlow<ProductUiState> =
        MutableStateFlow(
            ProductUiState.Data(
                ProductInfo(
                    title = "iPhone 9",
                    description = "An apple mobile which is nothing like apple",
                    price = 549,
                    discountPercentage = 12.96,
                    rating = 4.69,
                    stock = 94,
                    brand = "Apple",
                    category = "smartphones",
                    thumbnail = "https://cdn.dummyjson.com/product-images/1/thumbnail.jpg",
                    images = emptyList()
                )
            )
        )
}

sealed interface ProductUiState {
    data object Loading : ProductUiState

    data class Data(
        val product: ProductInfo
    ) : ProductUiState
}
