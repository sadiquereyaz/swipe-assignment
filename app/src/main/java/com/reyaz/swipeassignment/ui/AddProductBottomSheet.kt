package com.reyaz.swipeassignment.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import com.reyaz.swipeassignment.R
import com.reyaz.swipeassignment.data.db.entity.ProductEntity
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductBottomSheet(
    viewModel: ProductViewModel = koinViewModel(),
    onDismiss: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    // Derive unique product types from existing products
    val uniqueProductTypes by remember(uiState.products) {
        derivedStateOf {
            uiState.products
                .map { it.productType }
                .distinct()
                .sorted()
        }
    }

    var productName by remember { mutableStateOf("") }
    var productType by remember { mutableStateOf("") }
    var isProductTypeDropdownExpanded by remember { mutableStateOf(false) }
    var price by remember { mutableStateOf("") }
    var tax by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var isUploading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var currentStep by remember { mutableStateOf(0) }

    // Animated progress
    val animatedProgress by animateFloatAsState(
        targetValue = when (currentStep) {
            0 -> 0.0f
            1 -> 0.33f
            2 -> 0.66f
            3 -> 0.99f
            else -> 1f
        },
        label = "Progress Animation"
    )

    // Image launcher
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(),
        modifier = Modifier.fillMaxHeight(0.95f)
    ) {
        // Progress Indicator
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(4) { step ->
                    Box(
                        modifier = Modifier
                            .zIndex(1f)
                            .size(24.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(
                                if (step <= currentStep)
                                    MaterialTheme.colorScheme.primary
                                else
                                    ProgressIndicatorDefaults.linearTrackColor
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${step + 1}",
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
                    .zIndex(-1f),
                color = MaterialTheme.colorScheme.primary,
                progress = { animatedProgress },
            )
        }

        // Content Column with Scroll
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Step 1: Product Details
            if (currentStep == 0) {
                /*OutlinedTextField(
                    value = productName,
                    onValueChange = {
                        productName = it
                        errorMessage = null
                    },
                    label = { Text("Product Name") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.fillMaxWidth()
                )*/
                OutlinedTextField(
                    value = productName,
                    onValueChange = {
                        productName = it
                        errorMessage = null
                    },
                    label = { Text("Product Name") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next,
                        capitalization = KeyboardCapitalization.Words
                    ),
                    maxLines = 1,
                    keyboardActions = KeyboardActions(
                        onNext = {
                            // Move focus to product type dropdown or validate and move to next step
                            if (productName.isNotBlank()) {
                                isProductTypeDropdownExpanded = true
                            }
                        }
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                // Product Type Dropdown
                ExposedDropdownMenuBox(
                    expanded = isProductTypeDropdownExpanded,
                    onExpandedChange = {
                        isProductTypeDropdownExpanded = !isProductTypeDropdownExpanded
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = productType,
                        onValueChange = {
                            productType = it
                            errorMessage = null
                            // Allow custom entry or selection from dropdown
                            isProductTypeDropdownExpanded = true
                        },
                        label = { Text("Product Type") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = isProductTypeDropdownExpanded
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = {
                                if (productName.isBlank()) {
                                    errorMessage = "Product Name is required"
                                } else if (productType.isBlank()) {
                                    errorMessage = "Product Type is required"
                                } else
                                    currentStep++
                            }
                        ),
                    )

                    // Dropdown Menu
                    ExposedDropdownMenu(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        expanded = isProductTypeDropdownExpanded,
                        onDismissRequest = {
                            isProductTypeDropdownExpanded = false
                        }
                    ) {

                        // Optional: Add Custom Type Option
                        if (productType.isNotBlank() &&
                            !uniqueProductTypes.contains(productType)
                        ) {
                            DropdownMenuItem(
                                text = { Text("Add \"$productType\" as new type") },
                                onClick = {
                                    isProductTypeDropdownExpanded = false
                                },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                            )
                        }
                        // Existing Product Types
                        uniqueProductTypes.forEach { type ->
                            DropdownMenuItem(
                                text = { Text(type) },
                                onClick = {
                                    productType = type
                                    isProductTypeDropdownExpanded = false
                                },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                            )
                        }

                    }
                }
            }

            // Step 2: Pricing
            if (currentStep == 1) {
                OutlinedTextField(
                    value = price,
                    onValueChange = {
                        price = it
                        errorMessage = null
                    },
                    label = { Text("Price") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Next
                    ),

                    modifier = Modifier.fillMaxWidth(),
                )

                OutlinedTextField(
                    value = tax,
                    onValueChange = {
                        tax = it
                        errorMessage = null
                    },
                    label = { Text("Tax Rate") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            if (price.isBlank()) {
                                errorMessage = "Price is required"
                            } else if (tax.isBlank()) {
                                errorMessage = "Tax Rate is required"
                            } else
                                currentStep++
                        }
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Step 3: Image Selection
            if (currentStep == 2) {
                Button(
                    onClick = { launcher.launch("image/*") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Select Image")
                }

                // Display Selected Image
                if (imageUri != null) {
                    AsyncImage(
                        model = imageUri,
                        contentDescription = null,
                        modifier = Modifier
                            .size(200.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .align(Alignment.CenterHorizontally)
                    )
                }
            }

            // Step 4: Confirmation
            if (currentStep == 3) {
                Card(
                    modifier = Modifier,
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Box {
                        Column(
                            modifier = Modifier
                        ) {
                            AsyncImage(
                                model = imageUri,
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                                    .size(200.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                                placeholder = painterResource(R.drawable.ic_launcher_foreground),
                                onLoading = {},
                                contentScale = ContentScale.Crop
                            )


                            Column(modifier = Modifier.padding(8.dp, 0.dp, 8.dp, 8.dp)) {
                                Text(
                                    text = productName,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Price: ₹${price}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = "Tax: ${tax}%",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                        Text(
                            text = productType,
                            maxLines = 1,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier
                                .background(Color(0xFFCE7A00).copy(alpha = 0.9f))
                                .padding(end = 10.dp, start = 8.dp)
                                .align(alignment = Alignment.BottomEnd)
                        )
                    }
                }
                /*Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Product Summary", style = MaterialTheme.typography.titleLarge)
                    Text("Name: $productName")
                    Text("Type: $productType")
                    Text("Price: $price")
                    Text("Tax Rate: $tax")
                    if (imageUri != null) {
                        AsyncImage(
                            model = imageUri,
                            contentDescription = "Selected Product Image",
                            modifier = Modifier
                                .size(150.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )
                    }
                }*/
            }

            // Error Message
            if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    color = Color.Red,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Navigation Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Back Button (except for first step)
                if (currentStep > 0) {
                    Button(
                        onClick = {
                            if (currentStep > 0) currentStep--
                            errorMessage = null
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Back")
                    }
                }

                // Next/Add Button
                Button(
                    onClick = {
                        when (currentStep) {
                            0 -> {
                                // Validate Product Details
                                if (productName.isBlank()) {
                                    errorMessage = "Product Name is required"
                                    return@Button
                                }
                                if (productType.isBlank()) {
                                    errorMessage = "Product Type is required"
                                    return@Button
                                }
                                currentStep++
                            }

                            1 -> {
                                // Validate Pricing
                                if (price.isBlank()) {
                                    errorMessage = "Price is required"
                                    return@Button
                                }
                                if (tax.isBlank()) {
                                    errorMessage = "Tax Rate is required"
                                    return@Button
                                }
                                currentStep++
                            }

                            2 -> {
                                // Validate Image
                                if (imageUri == null) {
                                    errorMessage = "Please select an image"
                                    return@Button
                                }
                                currentStep++
                            }

                            3 -> {
                                // Final Submit
                                isUploading = true
                                try {
                                    viewModel.addProduct(
                                        productName,
                                        productType,
                                        price.toDoubleOrNull()
                                            ?: throw NumberFormatException("Invalid Price"),
                                        tax.toDoubleOrNull()
                                            ?: throw NumberFormatException("Invalid Tax Rate"),
                                        imageUri
                                    )
                                    onDismiss()
                                } catch (e: Exception) {
                                    errorMessage = "Failed to add product: ${e.localizedMessage}"
                                    isUploading = false
                                    currentStep = 2
                                }
                            }
                        }
                    },
                    enabled = !isUploading,
                    modifier = Modifier.weight(1f)
                ) {
                    if (isUploading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text(
                            text = when (currentStep) {
                                3 -> "Add Product"
                                else -> "Next"
                            }
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Next"
                        )
                    }
                }
            }
        }
    }
}