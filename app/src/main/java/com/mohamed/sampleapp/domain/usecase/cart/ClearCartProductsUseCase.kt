package com.mohamed.sampleapp.domain.usecase.cart

import com.mohamed.sampleapp.domain.repository.ProductsRepository
import javax.inject.Inject

class ClearCartProductsUseCase @Inject constructor(
    private val productsRepository: ProductsRepository
) {
    suspend operator fun invoke() {
        return productsRepository.clearCart()
    }
}