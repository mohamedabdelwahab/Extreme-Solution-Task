package com.mohamed.sampleapp.domain.datasource.remote

import com.mohamed.sampleapp.core.utilities.networkUtils.Resource
import com.mohamed.sampleapp.data.source.remote.reposnse.Product

interface RemoteDataSource {

    suspend fun getProductsByCategory(category: String): Resource<List<Product>>

    suspend fun getCategories(): Resource<List<String>>

    suspend fun getProductDetails(id: Int): Product
}