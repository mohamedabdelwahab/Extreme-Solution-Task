package com.mohamed.sampleapp.domain.usecase.category

import com.mohamed.sampleapp.core.utilities.networkUtils.Resource
import com.mohamed.sampleapp.domain.repository.ProductsRepository
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val productsRepository: ProductsRepository
) {
    suspend operator fun invoke(): Resource<List<String>> {
        return productsRepository.getCategories()
    }
}