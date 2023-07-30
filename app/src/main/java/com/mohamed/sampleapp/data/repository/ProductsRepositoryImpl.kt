package com.mohamed.sampleapp.data.repository

import com.mohamed.sampleapp.core.utilities.networkUtils.Resource
import com.mohamed.sampleapp.data.source.remote.reposnse.Product
import com.mohamed.sampleapp.domain.datasource.local.LocalDataSource
import com.mohamed.sampleapp.domain.datasource.remote.RemoteDataSource
import com.mohamed.sampleapp.domain.repository.ProductsRepository
import javax.inject.Inject

class ProductsRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : ProductsRepository {
    override suspend fun getProductsByCategory(category: String): Resource<List<Product>> {
        val cartList = localDataSource.getCartProducts().orEmpty()
        val remoteProducts = remoteDataSource.getProductsByCategory(category)
        if (remoteProducts is Resource.Success) {
            cartList.forEach { localItem ->
                remoteProducts.data.forEach { remoteItem ->
                    if (remoteItem.id == localItem.id) {
                        remoteItem.quantiy = localItem.quantiy
                        remoteItem.isSelected = true
                    }
                }
            }
        }
        return remoteProducts
    }

    override suspend fun getCategories() = remoteDataSource.getCategories()


    override suspend fun addToCart(product: Product) = localDataSource.addToCart(product)

    override suspend fun updateItem(product: Product) = localDataSource.updateItem(product)

    override suspend fun getCartProduts(): List<Product> =
        localDataSource.getCartProducts() ?: arrayListOf()

    override suspend fun deleteFromCart(id: Int) = localDataSource.deleteFromCart(id)

    override suspend fun clearCart() = localDataSource.clearCart()

}