package com.mohamed.sampleapp.domain.repository

import com.mohamed.sampleapp.core.utilities.networkUtils.Resource
import com.mohamed.sampleapp.data.source.remote.reposnse.Product

interface ProductsRepository {

    suspend fun getCategories(): Resource<List<String>>

    suspend fun getProductsByCategory(category: String): Resource<List<Product>>

    suspend fun addToCart(product: Product)

    suspend fun getCartProduts(): List<Product>?

    suspend fun deleteFromCart(id: Int)

    suspend fun clearCart()


    suspend fun updateItem(product: Product)
}