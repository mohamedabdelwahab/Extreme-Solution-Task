package com.mohamed.sampleapp.domain.datasource.local

import com.mohamed.sampleapp.data.source.remote.reposnse.Product

interface LocalDataSource {

    suspend fun addToCart(product: Product)

    suspend fun updateItem(product: Product)

    suspend fun getCartProducts(): List<Product>?

    suspend fun deleteFromCart(id: Int)

    suspend fun clearCart()

    suspend fun getCartProductIdsList(): List<Int>?

}