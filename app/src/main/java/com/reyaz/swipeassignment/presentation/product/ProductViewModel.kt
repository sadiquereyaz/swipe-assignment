package com.reyaz.swipeassignment.presentation.product

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reyaz.swipeassignment.data.db.entity.ProductEntity
import com.reyaz.swipeassignment.data.repository.ProductRepository
import com.reyaz.swipeassignment.domain.Resource
import kotlinx.coroutines.delay
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
        Log.d("VIEWMODEL", "init")
        loadProducts()
    }

    // Function to load products
    fun loadProducts() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) } // Show loading
            delay(1_000L)
            repository.getAllProducts().collect { result ->
                val products = result.data ?: emptyList()
                _uiState.update { state ->
                    when (result) {
                        is Resource.Success -> state.copy(
                            products = products,
                            searchList = filterProducts(products, state.searchQuery),
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

    // Function to update the search query and filter products
    fun updateSearchQuery(query: String) {
        _uiState.update { currentState ->
            currentState.copy(
                searchQuery = query,
                searchList = filterProducts(currentState.products, query)
            )
        }
    }

    // Helper function to filter products based on search query
    private fun filterProducts(products: List<ProductEntity>, query: String): List<ProductEntity> {
        return if (query.isBlank()) {
            products
        } else {
            products.filter { product ->
                product.productName.contains(query, ignoreCase = true) ||
                        product.productType.contains(query, ignoreCase = true) ||
                        product.price.toString().contains(query, ignoreCase = true)
            }
        }
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
//                    Log.d("VIEWMODEL", "addProduct: ${result}")
//                    Log.d("VIEWMODEL", "addProduct: ${result.message}")
                    loadProducts()
                }

                is Resource.Error -> {
                    _uiState.update { it.copy(
                        isLoading = false,
                        //error = result.message,
                        productAdditionError = result.message
                    ) }
                }

                is Resource.Loading -> {
                    // Handle loading state if needed
                    _uiState.update { it.copy(isLoading = true, error = null) }
                }
            }
        }
    }

    fun clearError() {
        _uiState.value.productAdditionError = null
    }


}
