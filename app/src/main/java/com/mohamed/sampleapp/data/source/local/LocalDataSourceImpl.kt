package com.mohamed.sampleapp.data.source.local

import com.mohamed.sampleapp.data.source.remote.reposnse.Product
import com.mohamed.sampleapp.domain.datasource.local.LocalDataSource
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class LocalDataSourceImpl @Inject constructor(
    private val productDAO: ProductDAO,
    private val ioDispatcher: CoroutineContext
) : LocalDataSource {

    override suspend fun addToCart(product: Product) = withContext(ioDispatcher) {
        productDAO.addToCart(product)
    }

    override suspend fun updateItem(product: Product) = withContext(ioDispatcher){
        productDAO.updateProduct(product)
    }

    override suspend fun getCartProducts(): List<Product>? = withContext(ioDispatcher) {
        productDAO.getCartProducts()
    }

    override suspend fun deleteFromCart(id: Int) = withContext(ioDispatcher) {
        productDAO.deleteFromCart(id)
    }

    override suspend fun clearCart() = withContext(ioDispatcher) {
        productDAO.clearFavorites()
    }

    override suspend fun getCartProductIdsList(): List<Int>? =
        productDAO.getCartProductsIds()

}