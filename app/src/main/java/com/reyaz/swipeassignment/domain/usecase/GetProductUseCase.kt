/*
package com.reyaz.swipeassignment.domain.usecase

import com.reyaz.swipeassignment.domain.repository.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.koin.core.annotation.Provided

class GetProductUseCase(
    private val productRepository: ProductRepository      //di injection
) {

    */
/*operator fun invoke(): Flow<String> = flow {
        emit(productRepository.getUserName())
    }*//*


    operator fun invoke() = flow {
        emit(productRepository.getUserName())
    }.catch {
        emit("error")
    }.flowOn(Dispatchers.IO)
}*/
