package com.alad1nks.productsandroid.feature.product

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.alad1nks.productsandroid.core.designsystem.components.ErrorScreen
import com.alad1nks.productsandroid.core.model.PokemonInfo

@Composable
internal fun ProductRoute(
    id: Int,
    onBackButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProductViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.observeAsState(ProductUiState.Loading)
    LaunchedEffect(id) {
        viewModel.refresh(id)
    }
    ProductScreen(
        uiState = uiState,
        onBackButtonClick = onBackButtonClick,
        onTryAgainClick = { viewModel.refresh(id) },
        modifier = modifier
    )
}

@Composable
internal fun ProductScreen(
    uiState: ProductUiState,
    onBackButtonClick: () -> Unit,
    onTryAgainClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            ProductTopBar(
                onBackButtonClick = onBackButtonClick
            )
        }
    ) { padding ->
        ProductContent(
            uiState = uiState,
            onTryAgainClick = onTryAgainClick,
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ProductTopBar(
    onBackButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var clicked by remember { mutableStateOf(false) }
    TopAppBar(
        title = {
            Text(stringResource(R.string.placeholder_product))
        },
        modifier = modifier,
        navigationIcon = {
            IconButton(
                onClick = {
                    if (!clicked) {
                        clicked = true
                        onBackButtonClick()
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.icon_navigate_back)
                )
            }
        }
    )
}

@Composable
internal fun ProductContent(
    uiState: ProductUiState,
    onTryAgainClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (uiState) {
        is ProductUiState.Data -> {
            ProductData(
                pokemon = uiState.product,
                modifier = modifier
            )
        }

        ProductUiState.Loading -> {
            ProductLoading(
                modifier = modifier
            )
        }

        ProductUiState.Error -> {
            ProductError(
                onTryAgainClick = onTryAgainClick,
                modifier = modifier
            )
        }
    }
}

@Composable
internal fun ProductData(
    pokemon: PokemonInfo,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
    ) {
        item {
            ProductImages(
                images = pokemon.sprites,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
        item {
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                ProductCharacteristic(
                    parameter = "Height",
                    value = pokemon.height,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                ProductCharacteristic(
                    parameter = "Weight",
                    value = pokemon.weight,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
    }
}

@Composable
internal fun ProductLoading(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
internal fun ProductError(
    onTryAgainClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ErrorScreen(
        onTryAgainClick = onTryAgainClick,
        modifier = modifier
    )
}

@Composable
internal fun ProductImages(
    images: List<String>,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        item {
            images.forEach { image ->
                AsyncImage(
                    model = image,
                    contentDescription = null,
                    modifier = Modifier
                        .height(200.dp),
                    contentScale = ContentScale.FillHeight
                )
            }
        }
    }
}

@Composable
internal fun ProductCharacteristic(
    parameter: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
    ) {
        Text(
            text = parameter,
            modifier = Modifier
                .weight(1f),
            fontWeight = FontWeight.Light
        )
        Text(
            text = value,
            modifier = Modifier
                .weight(1f),
            fontWeight = FontWeight.Medium
        )
    }
}
