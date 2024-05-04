package com.alad1nks.productsandroid.feature.products

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
internal fun ProductsRoute(
    onShowSnackbar: suspend (String, String?) -> Boolean,
    modifier: Modifier = Modifier,
    viewModel: ProductsViewModel = hiltViewModel()
) {
}
