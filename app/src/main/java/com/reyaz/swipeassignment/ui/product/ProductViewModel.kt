package com.reyaz.swipeassignment.ui.product

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reyaz.swipeassignment.data.remote.api.RetrofitClient
import com.reyaz.swipeassignment.domain.model.Product
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class ProductViewModel : ViewModel() {
    private val _products = mutableStateListOf<Product>()
    val products: List<Product> get() = _products

    private val _isLoading = mutableStateOf(false)
    val isLoading: Boolean get() = _isLoading.value

    private val _error = mutableStateOf<String?>(null)
    val error: String? get() = _error.value

    init {
        loadProducts()
    }

    private fun loadProducts() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = RetrofitClient.instance.getProducts()
                _products.clear()
                _products.addAll(response)
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addProduct(product: Product, image: MultipartBody.Part?) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val productName = product.product_name.toRequestBody()
                val productType = product.product_type.toRequestBody()
                val price = product.price.toString().toRequestBody()
                val tax = product.tax.toString().toRequestBody()

                val response = RetrofitClient.instance.addProduct(
                    productName,
                    productType,
                    price,
                    tax,
                    image?.let { listOf(it) }
                )

                if (response.success) {
                    _products.add(response.product_details)
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}