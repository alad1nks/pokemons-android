package com.alad1nks.productsandroid.feature.products

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import com.alad1nks.productsandroid.core.model.Product

@Composable
internal fun ProductsRoute(
    onShowSnackbar: suspend (String, String?) -> Boolean,
    onClickItem: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProductsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.observeAsState(initial = ProductsUiState.Loading)

    ProductsScreen(
        onShowSnackbar = onShowSnackbar,
        onClickItem = onClickItem,
        uiState = uiState,
        modifier = modifier
    )
}

@Composable
internal fun ProductsScreen(
    onShowSnackbar: suspend (String, String?) -> Boolean,
    onClickItem: (Int) -> Unit,
    uiState: ProductsUiState,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            ProductsTopBar(Modifier)
        }
    ) { padding ->
        when (uiState) {
            is ProductsUiState.Data -> {
                ProductList(
                    products = uiState.products,
                    onClickItem = onClickItem,
                    modifier = Modifier
                        .padding(padding)
                )
            }
            ProductsUiState.Loading -> {

            }
            ProductsUiState.Error -> {

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ProductsTopBar(
    modifier: Modifier
) {
    TopAppBar(
        title = {
            Text(stringResource(R.string.products))
        },
        modifier = modifier,
        actions = {
            IconButton(onClick = {  }) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = stringResource(R.string.search_icon)
                )
            }
            IconButton(onClick = {  }) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = stringResource(R.string.more_icon)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        )
    )
}

@Composable
internal fun ProductList(
    products: List<Product>,
    onClickItem: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(products) { product ->
            ListItem(
                headlineContent = {
                    Text(
                        text = product.title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                modifier = Modifier.clickable {
                    onClickItem(product.id)
                },
                supportingContent = {
                    Text(
                        text = product.description,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                trailingContent = { Text(product.price.toString()) },
                leadingContent = {
                    Icon(
                        Icons.Filled.Favorite,
                        contentDescription = null
                    )
                }
            )
        }
    }
}
