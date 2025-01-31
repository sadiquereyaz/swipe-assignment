/*
package com.reyaz.swipeassignment.ui.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.reyaz.swipeassignment.domain.model.Product
import com.reyaz.swipeassignment.ui.product.ProductViewModel

@Composable
fun ProductListingScreen(viewModel: ProductViewModel, onAddProductClick: () -> Unit) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddProductClick) {
                Icon(Icons.Default.Add, contentDescription = "Add Product")
            }
        }
    ) { padding ->
        if (viewModel.isLoading) {
            CircularProgressIndicator(modifier = Modifier.fillMaxSize().wrapContentSize())
        } else if (viewModel.error != null) {
            Text(text = "Error: ${viewModel.error}", color = Color.Red)
        } else {
            LazyColumn(modifier = Modifier.padding(padding)) {
                items(viewModel.products) { product ->
                    ProductItem(product = product)
                }
            }
        }
    }
}

@Composable
fun ProductItem(product: Product) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            AsyncImage(
                model = product.image,
                contentDescription = null,
                modifier = Modifier.size(100.dp)
            )
            Text(text = product.product_name, style = MaterialTheme.typography.headlineSmall)
            Text(text = "Price: $${product.price}", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Tax: ${product.tax}%", style = MaterialTheme.typography.bodyMedium)
        }
    }
}*/
