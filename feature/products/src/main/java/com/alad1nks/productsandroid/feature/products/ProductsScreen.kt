package com.alad1nks.productsandroid.feature.products

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
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
        onScroll = { viewModel.loadMore(it) },
        onClickRefresh = { viewModel.load() },
        uiState = uiState,
        modifier = modifier
    )
}

@Composable
internal fun ProductsScreen(
    onShowSnackbar: suspend (String, String?) -> Boolean,
    onClickItem: (Int) -> Unit,
    onScroll: (Int) -> Unit,
    onClickRefresh: () -> Unit,
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
                    onScroll = onScroll,
                    modifier = Modifier
                        .padding(padding)
                )
            }
            ProductsUiState.Loading -> {

            }
            ProductsUiState.Error -> {
                ProductsErrorScreen(
                    onClick = onClickRefresh,
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize()
                )
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
            titleContentColor = MaterialTheme.colorScheme.primary
        )
    )
}

@Composable
internal fun ProductList(
    products: List<Product>,
    onClickItem: (Int) -> Unit,
    onScroll: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
    ) {
        itemsIndexed(products) { index, product ->
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
                    AsyncImage(
                        model = product.thumbnail,
                        contentDescription = null,
                        modifier = Modifier
                            .size(60.dp)
                            .clip(RoundedCornerShape(30)),
                        contentScale = ContentScale.Crop
                    )
                }
            )
            if (index >= products.size - 1) {
                onScroll(products.size)
            }
        }
    }
}

@Composable
internal fun ProductsErrorScreen(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isSystemInDarkTheme = isSystemInDarkTheme()
    val imageRes = if (isSystemInDarkTheme) {
        R.drawable.network_error
    } else {
        R.drawable.network_error
    }
    val background = if (isSystemInDarkTheme) Color.Black else Color.White
    Column(
        modifier = modifier
            .background(background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(imageRes),
            contentDescription = stringResource(R.string.network_error_image)
        )
        Button(
            onClick = onClick,
            content = { Text(stringResource(R.string.try_again)) }
        )
    }
}
