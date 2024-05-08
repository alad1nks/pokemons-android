package com.alad1nks.productsandroid.feature.product

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.alad1nks.productsandroid.core.model.ProductInfo

@Composable
internal fun ProductRoute(
    id: Int,
    onClickBackButton: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProductViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.observeAsState(ProductUiState.Loading)
    LaunchedEffect(id) {
        viewModel.refresh(id)
    }
    ProductScreen(
        uiState = uiState,
        onClickBackButton = onClickBackButton,
        modifier = modifier
    )
}

@Composable
internal fun ProductScreen(
    uiState: ProductUiState,
    onClickBackButton: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            ProductTopBar(
                onClickBackButton = onClickBackButton
            )
        }
    ) { padding ->
        ProductContent(
            uiState = uiState,
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ProductTopBar(
    onClickBackButton: () -> Unit,
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
                        onClickBackButton()
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
    modifier: Modifier = Modifier
) {
    when (uiState) {
        is ProductUiState.Data -> {
            ProductData(
                product = uiState.product,
                modifier = modifier
            )
        }

        ProductUiState.Loading -> {
            ProductLoading(
                modifier = modifier
            )
        }

        ProductUiState.Error -> {

        }
    }
}

@Composable
internal fun ProductData(
    product: ProductInfo,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
    ) {
        item {
            ProductImages(
                images = product.images,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
        item {
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.description),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(product.description)
                Spacer(modifier = Modifier.height(8.dp))
                ProductCharacteristic(
                    parameter = "Category",
                    value = product.category,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                ProductCharacteristic(
                    parameter = "Brand",
                    value = product.brand,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                ProductCharacteristic(
                    parameter = "Stock",
                    value = product.stock.toString(),
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
