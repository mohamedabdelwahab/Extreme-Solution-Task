package com.mohamed.sampleapp.domain.usecase.products

import com.mohamed.sampleapp.data.source.remote.reposnse.Product
import com.mohamed.sampleapp.domain.repository.ProductsRepository
import javax.inject.Inject

class RemoveProductUseCase @Inject constructor(
    private val productsRepository: ProductsRepository
) {
    suspend operator fun invoke(product: Product) {
        return productsRepository.deleteFromCart(product.id!!)
    }
}