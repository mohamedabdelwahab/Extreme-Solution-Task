package com.mohamed.sampleapp.data.source.remote

import com.mohamed.sampleapp.data.source.remote.reposnse.Product
import retrofit2.Response
import retrofit2.http.*

interface ProductService {

    @GET("products/categories")
    suspend fun getCategories(): Response<List<String>>

    @GET("products/category/{category}")
    suspend fun getProductsByCategory(@Path("category") category: String): Response<List<Product>>

    @GET("products/{id}")
    suspend fun getProductDetails(@Path("id") id: Int): Product

}