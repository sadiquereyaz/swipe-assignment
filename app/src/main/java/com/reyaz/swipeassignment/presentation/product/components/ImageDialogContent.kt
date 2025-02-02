package com.reyaz.swipeassignment.presentation.product.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.reyaz.swipeassignment.R
import com.reyaz.swipeassignment.data.db.entity.ProductEntity


@Composable
fun ImageDialogContent(
    product: ProductEntity,
    onDismiss: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(
            onClick = {
                onDismiss(false)
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Icon(Icons.Default.Close, "", tint = Color.White)
        }
        product.image?.let {
            ZoomableImageContainer(it)
        } ?: Icon(
            imageVector = ImageVector.vectorResource(R.drawable.logo),
            contentDescription = "upload image",
            tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            modifier = Modifier
                .size(200.dp)
        )
    }
}
