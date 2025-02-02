package com.reyaz.swipeassignment.data.api.dto

sealed class ApiResponse<out T> {
    data class Success<T>(
        val data: T,
        val message: String
    ) : ApiResponse<T>()
    
    data class Error(
        val code: Int,
        val message: String
    ) : ApiResponse<Nothing>()
}