package com.alad1nks.productsandroid.feature.products

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.alad1nks.productsandroid.core.designsystem.components.SearchBar
import com.alad1nks.productsandroid.core.model.Product

@Composable
internal fun ProductsRoute(
    onShowSnackbar: suspend (String, String?) -> Boolean,
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProductsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.observeAsState(initial = ProductsUiState.Loading)
    val darkTheme by viewModel.darkTheme.collectAsState(initial = false)
    val searchQuery by viewModel.searchQuery.observeAsState(initial = "")

    ProductsScreen(
        darkTheme = darkTheme,
        onThemeIconClick = { viewModel.changeTheme() },
        searchValue = searchQuery,
        onSearchValueChange = { viewModel.search(it) },
        onItemClick = onItemClick,
        onScroll = { viewModel.loadMore(it) },
        onRefreshClick = { viewModel.refresh() },
        uiState = uiState,
        modifier = modifier
    )
}

@Composable
internal fun ProductsScreen(
    darkTheme: Boolean,
    onThemeIconClick: () -> Unit,
    searchValue: String,
    onSearchValueChange: (String) -> Unit,
    onItemClick: (Int) -> Unit,
    onScroll: (Int) -> Unit,
    onRefreshClick: () -> Unit,
    uiState: ProductsUiState,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            ProductsTopBar(
                darkTheme = darkTheme,
                onThemeIconClick = onThemeIconClick,
                searchValue = searchValue,
                onSearchValueChange = onSearchValueChange
            )
        }
    ) { padding ->
        when (uiState) {
            is ProductsUiState.Data -> {
                ProductList(
                    products = uiState.products,
                    onClickItem = onItemClick,
                    onScroll = onScroll,
                    modifier = Modifier
                        .padding(padding)
                )
            }

            ProductsUiState.Loading -> {

            }

            ProductsUiState.Error -> {
                ProductsErrorScreen(
                    darkTheme = darkTheme,
                    onClick = onRefreshClick,
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
    darkTheme: Boolean,
    onThemeIconClick: () -> Unit,
    searchValue: String,
    onSearchValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showSearchBar by remember { mutableStateOf(searchValue.isNotEmpty()) }

    TopAppBar(
        title = {
            if (!showSearchBar) {
                Text(stringResource(R.string.products))
            }

            AnimatedVisibility(
                visible = showSearchBar,
                enter = fadeIn() + expandHorizontally(),
                exit = fadeOut() + shrinkHorizontally()
            ) {
                SearchBar(
                    value = searchValue,
                    onValueChange = onSearchValueChange,
                    onSearchClose = {
                        onSearchValueChange("")
                        showSearchBar = false
                    }
                )
            }
        },
        modifier = modifier,
        navigationIcon = {
            IconButton(onClick = onThemeIconClick) {
                Icon(
                    imageVector = if (darkTheme) Icons.Filled.DarkMode else Icons.Filled.LightMode,
                    contentDescription = stringResource(R.string.icon_app_theme)
                )
            }
        },
        actions = {
            AnimatedVisibility(
                visible = !showSearchBar,
                enter = fadeIn() + expandHorizontally(),
                exit = fadeOut() + shrinkHorizontally()
            ) {
                IconButton(onClick = { showSearchBar = true }) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = stringResource(R.string.search_icon)
                    )
                }
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
    darkTheme: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val imageRes = if (darkTheme) {
        R.drawable.network_error_dark
    } else {
        R.drawable.network_error
    }
    val background = if (darkTheme) Color.Black else Color.White
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
