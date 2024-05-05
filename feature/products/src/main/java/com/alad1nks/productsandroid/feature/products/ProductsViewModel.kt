package com.alad1nks.productsandroid.feature.products

import androidx.lifecycle.ViewModel
import com.alad1nks.productsandroid.core.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor() : ViewModel() {
    val uiState: StateFlow<ProductsUiState> =
        MutableStateFlow(
            ProductsUiState.Data(
                List(20) {
                    Product(
                        id = it.toLong(),
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
                }
            )
        )
}

sealed interface ProductsUiState {
    data object Loading : ProductsUiState

    data class Data(
        val products: List<Product>
    ) : ProductsUiState
}
