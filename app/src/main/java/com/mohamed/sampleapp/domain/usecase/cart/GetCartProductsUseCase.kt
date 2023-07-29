package com.mohamed.sampleapp.domain.usecase.cart

import com.mohamed.sampleapp.data.source.remote.reposnse.Product
import com.mohamed.sampleapp.domain.repository.ProductsRepository
import javax.inject.Inject

class GetCartProductsUseCase @Inject constructor(
    private val productsRepository: ProductsRepository
) {
    suspend operator fun invoke(): List<Product> {
        return productsRepository.getCartProduts() ?: arrayListOf()
    }
}