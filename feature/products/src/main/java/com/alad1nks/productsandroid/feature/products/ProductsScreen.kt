package com.alad1nks.productsandroid.feature.products

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import com.alad1nks.productsandroid.core.designsystem.components.AnimatedShimmerListItem
import com.alad1nks.productsandroid.core.designsystem.components.ErrorScreen
import com.alad1nks.productsandroid.core.model.Pokemon

@Composable
internal fun ProductsRoute(
    onShowSnackbar: suspend (String, String?) -> Boolean,
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProductsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.observeAsState(initial = ProductsUiState.Loading)
    val darkTheme by viewModel.darkTheme.collectAsState(initial = false)
    val shouldEndRefresh by viewModel.shouldEndRefresh.observeAsState(initial = false)

    ProductsScreen(
        onSwipe = { viewModel.refresh(true) },
        shouldEndRefresh = shouldEndRefresh,
        onRefreshEnded = { viewModel.onRefreshEnded() },
        onThemeIconClick = { viewModel.changeTheme() },
        uiState = uiState,
        onItemClick = onItemClick,
        onScroll = { viewModel.loadMore(it) },
        darkTheme = darkTheme,
        onTryAgainClick = { viewModel.refresh() },
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ProductsScreen(
    onSwipe: () -> Unit,
    shouldEndRefresh: Boolean,
    onRefreshEnded: () -> Unit,
    onThemeIconClick: () -> Unit,
    uiState: ProductsUiState,
    onItemClick: (Int) -> Unit,
    onScroll: (Int) -> Unit,
    darkTheme: Boolean,
    onTryAgainClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val refreshState = rememberPullToRefreshState()
    if (refreshState.isRefreshing) {
        LaunchedEffect(true) {
            onSwipe()
        }
    }
    if (shouldEndRefresh) {
        LaunchedEffect(true) {
            onRefreshEnded()
            refreshState.endRefresh()
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            ProductsTopBar(
                darkTheme = darkTheme,
                onThemeIconClick = onThemeIconClick
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .nestedScroll(refreshState.nestedScrollConnection)
                .padding(padding)
        ) {
            ProductsContent(
                uiState = uiState,
                onItemClick = onItemClick,
                onScroll = onScroll,
                onTryAgainClick = onTryAgainClick,
                modifier = Modifier
                    .fillMaxSize()
            )
            PullToRefreshContainer(
                modifier = Modifier
                    .align(Alignment.TopCenter),
                state = refreshState
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ProductsTopBar(
    darkTheme: Boolean,
    onThemeIconClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Text(stringResource(R.string.pokemons_title))
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
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary
        )
    )
}

@Composable
internal fun ProductsContent(
    uiState: ProductsUiState,
    onItemClick: (Int) -> Unit,
    onScroll: (Int) -> Unit,
    onTryAgainClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (uiState) {
        is ProductsUiState.Data -> {
            ProductsData(
                pokemons = uiState.pokemons,
                onClickItem = onItemClick,
                onScroll = onScroll,
                modifier = modifier
            )
        }

        ProductsUiState.Loading -> {
            ProductsLoading(
                modifier = modifier
            )
        }

        ProductsUiState.Error -> {
            ProductsError(
                onTryAgainClick = onTryAgainClick,
                modifier = modifier
            )
        }
    }
}

@Composable
internal fun ProductsData(
    pokemons: List<Pokemon>,
    onClickItem: (Int) -> Unit,
    onScroll: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
    ) {
        itemsIndexed(pokemons) { index, product ->
            ListItem(
                headlineContent = {
                    Text(
                        text = product.name,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                modifier = Modifier
                    .clickable { onClickItem(index + 1) }
            )
            if (index >= pokemons.size - 1) {
                onScroll(pokemons.size)
            }
        }
    }
}

@Composable
internal fun ProductsLoading(
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(20) {
            AnimatedShimmerListItem()
        }
    }
}

@Composable
internal fun ProductsError(
    onTryAgainClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ErrorScreen(
        onTryAgainClick = onTryAgainClick,
        modifier = modifier
    )
}
