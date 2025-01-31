package com.reyaz.swipeassignment.ui

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reyaz.swipeassignment.data.db.entity.ProductEntity
import com.reyaz.swipeassignment.data.repository.ProductRepository
import com.reyaz.swipeassignment.domain.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductViewModel(
    private val repository: ProductRepository
) : ViewModel() {

    // UI State
    private val _uiState = MutableStateFlow(ProductUiState())
    val uiState: StateFlow<ProductUiState> = _uiState.asStateFlow()

    // Fetch all products on initialization
    init {
        loadProducts()
    }

    // Function to load products
    private fun loadProducts() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) } // Show loading
            repository.getAllProducts().collect { result ->
                _uiState.update { state ->
                    when (result) {
                        is Resource.Success -> state.copy(
                            products = result.data ?: emptyList(),
                            isLoading = false,
                            error = null
                        )
                        is Resource.Error -> state.copy(
                            isLoading = false,
                            error = result.message
                        )
                        is Resource.Loading -> state.copy(
                            isLoading = true,
                            error = null
                        )
                    }
                }
            }
        }
    }

    // Function to update the search query
    fun updateSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    // Function to add a new product
    fun addProduct(
        productName: String,
        productType: String,
        price: Double,
        tax: Double,
        imageUri: Uri?
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) } // Show loading
            val result = repository.addProduct(
                productName = productName,
                productType = productType,
                price = price,
                tax = tax,
                imageUri = imageUri
            )
            when (result) {
                is Resource.Success -> {
                    // Reload products after adding a new one
                    loadProducts()
                }
                is Resource.Error -> {
                    _uiState.update { it.copy(isLoading = false, error = result.message) }
                }
                is Resource.Loading -> {
                    // Handle loading state if needed
                }
            }
        }
    }


}
// State class
data class ProductUiState(
    val products: List<ProductEntity> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = ""
)