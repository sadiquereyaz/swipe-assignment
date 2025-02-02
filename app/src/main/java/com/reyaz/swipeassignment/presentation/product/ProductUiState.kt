package com.reyaz.swipeassignment.presentation.product

import com.reyaz.swipeassignment.data.db.entity.ProductEntity


data class ProductUiState(
    val products: List<ProductEntity> = emptyList(),
    val searchList: List<ProductEntity> = emptyList(),
    val isLoading: Boolean = false,
    var error: String? = null,
    val searchQuery: String = "",
    var unViewedCount: Int = 0

)