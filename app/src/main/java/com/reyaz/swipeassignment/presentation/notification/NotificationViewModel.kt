package com.reyaz.swipeassignment.presentation.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reyaz.swipeassignment.domain.repository.NotificationRepository
import com.reyaz.swipeassignment.domain.model.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NotificationViewModel(
    private val repository: NotificationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(NotificationUiState())
    val uiState: StateFlow<NotificationUiState> = _uiState.asStateFlow()

    init {
        fetchAll()
        viewModelScope.launch{ repository.markAsViewed() }
    }

    fun fetchAll() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            repository.getAll().collect { result ->
                val products = result.data ?: emptyList()
                _uiState.update { state ->
                    when (result) {
                        is Resource.Success -> {
                            state.copy(
                                notificationList = products,
                                isLoading = false,
                                error = null
                            )
                        }

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
}
