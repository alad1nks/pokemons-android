package com.alad1nks.productsandroid.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.alad1nks.productsandroid.feature.products.navigation.PRODUCTS_ROUTE
import com.alad1nks.productsandroid.feature.products.navigation.productsScreen

@Composable
fun NavHost(
    onShowSnackbar: suspend (String, String?) -> Boolean,
    modifier: Modifier = Modifier,
    startDestination: String = PRODUCTS_ROUTE
) {
    val navController: NavHostController = rememberNavController()
    androidx.navigation.compose.NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        productsScreen(onShowSnackbar)
    }
}
