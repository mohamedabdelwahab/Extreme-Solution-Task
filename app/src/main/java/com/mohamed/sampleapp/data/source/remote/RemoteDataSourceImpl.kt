package com.mohamed.sampleapp.data.source.remote

import com.mohamed.sampleapp.core.utilities.networkUtils.Resource
import com.mohamed.sampleapp.core.utilities.networkUtils.apiCall
import com.mohamed.sampleapp.data.source.remote.reposnse.Product
import com.mohamed.sampleapp.domain.datasource.remote.RemoteDataSource
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(
    private val productService: ProductService,
) : RemoteDataSource {

    override suspend fun getProductsByCategory(category: String): Resource<List<Product>> {
        return apiCall { productService.getProductsByCategory(category)}
    }


    override suspend fun getCategories(): Resource<List<String>> {
        return apiCall { productService.getCategories() }
    }


    override suspend fun getProductDetails(id: Int): Product =
        productService.getProductDetails(id)

}