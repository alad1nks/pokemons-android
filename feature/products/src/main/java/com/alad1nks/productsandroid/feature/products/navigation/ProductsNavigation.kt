package com.alad1nks.productsandroid.feature.products.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.alad1nks.productsandroid.feature.products.ProductsRoute

const val PRODUCTS_ROUTE = "products_route"

fun NavGraphBuilder.productsScreen(
    onShowSnackbar: suspend (String, String?) -> Boolean
) {
    composable(PRODUCTS_ROUTE) {
        ProductsRoute(onShowSnackbar)
    }
}