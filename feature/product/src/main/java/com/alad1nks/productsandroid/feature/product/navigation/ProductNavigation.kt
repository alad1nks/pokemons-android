package com.alad1nks.productsandroid.feature.product.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.alad1nks.productsandroid.feature.product.ProductRoute

const val PRODUCT_ROUTE = "product_route/{id}"

fun NavGraphBuilder.productScreen(
    onClickBackButton: () -> Unit
) {
    composable(
        route = PRODUCT_ROUTE,
        arguments = listOf(navArgument("id") { type = NavType.IntType })

    ) {backStackEntry ->
        ProductRoute(
            id = backStackEntry.arguments?.getInt("id") ?: 0,
            onBackButtonClick = onClickBackButton
        )
    }
}
