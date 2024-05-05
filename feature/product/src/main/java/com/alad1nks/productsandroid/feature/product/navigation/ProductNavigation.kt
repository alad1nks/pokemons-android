package com.alad1nks.productsandroid.feature.product.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.alad1nks.productsandroid.feature.product.ProductRoute

const val PRODUCT_ROUTE = "product_route"

fun NavGraphBuilder.productScreen(
    onShowSnackbar: suspend (String, String?) -> Boolean
) {
    composable(PRODUCT_ROUTE) {
        ProductRoute(onShowSnackbar)
    }
}
