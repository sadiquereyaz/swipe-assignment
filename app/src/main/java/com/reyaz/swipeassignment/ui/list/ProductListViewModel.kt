/*
package com.reyaz.swipeassignment.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reyaz.swipeassignment.domain.usecase.GetProductUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class ProductListViewModel(
    private val getProductUseCase: GetProductUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    init {
        getUserName()
    }

    private fun getUserName() = getProductUseCase()
        .onEach { result ->
            _uiState.update { UiState(data = result) }
        }.catch { error ->
            _uiState.update { UiState(error = error.message.toString()) }
        }.onStart {
            _uiState.update { UiState(isLoading = true) }
        }.launchIn(viewModelScope)

}
*/
