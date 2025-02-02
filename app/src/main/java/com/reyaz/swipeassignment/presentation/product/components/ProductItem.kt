package com.reyaz.swipeassignment.presentation.product.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.reyaz.swipeassignment.R
import com.reyaz.swipeassignment.data.db.entity.ProductEntity

@Composable
fun ProductItem(product: ProductEntity) {

    var showDialog by remember { mutableStateOf(false) }
    Card(
        onClick = {if (product.image != "") showDialog = true},
        modifier = Modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box {
            Column(
                modifier = Modifier
            ) {
                AsyncImage(
                    model = if (product.image != "") product.image else R.drawable.no_image,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .height(200.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    placeholder = painterResource(R.drawable.ic_launcher_foreground),
                    onLoading = {},
                    contentScale = if (product.image != "") ContentScale.Crop else ContentScale.Inside
                )

                Column(modifier = Modifier.padding(8.dp, 0.dp, 8.dp, 8.dp)) {
                    Text(
                        text = product.productName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "Price: ₹${product.price}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(text = "Tax: ${product.tax}%", style = MaterialTheme.typography.bodySmall)
                }
            }
            Text(
                text = product.productType,
                maxLines = 1,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .background(Color(0xFFCE7A00).copy(alpha = 0.9f))
                    .padding(end = 10.dp, start = 8.dp)
                    .align(alignment = Alignment.BottomEnd)
            )
        }
    }
    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            ImageDialogContent(product) { showDialog = false }
        }
    }
}
