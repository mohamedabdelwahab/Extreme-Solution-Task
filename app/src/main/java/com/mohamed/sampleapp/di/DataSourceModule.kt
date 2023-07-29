package com.mohamed.sampleapp.di

import com.mohamed.sampleapp.data.source.local.LocalDataSourceImpl
import com.mohamed.sampleapp.data.source.local.ProductDAO
import com.mohamed.sampleapp.data.source.remote.ProductService
import com.mohamed.sampleapp.data.source.remote.RemoteDataSourceImpl
import com.mohamed.sampleapp.domain.datasource.local.LocalDataSource
import com.mohamed.sampleapp.domain.datasource.remote.RemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    @Singleton
    fun provideRemoteDataSource(
        productService: ProductService,
    ): RemoteDataSource = RemoteDataSourceImpl(productService)

    @Provides
    @Singleton
    fun provideLocalDataSource(
        productDAO: ProductDAO,
        ioDispatcher: CoroutineContext
    ): LocalDataSource = LocalDataSourceImpl(productDAO, ioDispatcher)

}