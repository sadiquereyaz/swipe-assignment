package com.reyaz.swipeassignment.ui

import com.reyaz.swipeassignment.data.db.entity.ProductEntity

// State class
data class ProductUiState(
    val products: List<ProductEntity> = emptyList(),
    val searchList: List<ProductEntity> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = ""
)