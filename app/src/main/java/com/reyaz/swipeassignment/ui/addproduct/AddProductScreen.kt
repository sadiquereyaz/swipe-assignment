package com.reyaz.swipeassignment.ui.addproduct

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.reyaz.swipeassignment.domain.model.Product
import com.reyaz.swipeassignment.ui.product.ProductViewModel
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductScreen(viewModel: ProductViewModel, onClose: () -> Unit) {
    var productName by remember { mutableStateOf("") }
    var productType by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var tax by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Product") },
                actions = {
                    IconButton(onClick = onClose) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            OutlinedTextField(
                value = productName,
                onValueChange = { productName = it },
                label = { Text("Product Name") }
            )
            OutlinedTextField(
                value = productType,
                onValueChange = { productType = it },
                label = { Text("Product Type") }
            )
            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Price") }
            )
            OutlinedTextField(
                value = tax,
                onValueChange = { tax = it },
                label = { Text("Tax") }
            )
            Button(onClick = {
                val product = Product(
                    image = "",
                    price = price.toDoubleOrNull() ?: 0.0,
                    product_name = productName,
                    product_type = productType,
                    tax = tax.toDoubleOrNull() ?: 0.0
                )
                val imagePart = imageUri?.let { uri ->
                    val file = File(uri.path)
                    MultipartBody.Part.createFormData("files[]", file.name, file.asRequestBody())
                }
                viewModel.addProduct(product, imagePart)
                onClose()
            }) {
                Text("Submit")
            }
        }
    }
}