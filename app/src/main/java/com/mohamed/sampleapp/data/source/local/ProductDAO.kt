package com.mohamed.sampleapp.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.mohamed.sampleapp.data.source.remote.reposnse.Product

@Dao
interface ProductDAO {

    @Insert
    suspend fun addToCart(product: Product)

    @Update
    fun updateProduct(product: Product)

    @Query("SELECT * FROM products")
    suspend fun getCartProducts(): List<Product>?

    @Query("SELECT id FROM products")
    suspend fun getCartProductsIds(): List<Int>?

    @Query("DELETE FROM products WHERE id = :idInput")
    suspend fun deleteFromCart(idInput: Int)

    @Query("DELETE FROM products")
    fun clearFavorites()
}