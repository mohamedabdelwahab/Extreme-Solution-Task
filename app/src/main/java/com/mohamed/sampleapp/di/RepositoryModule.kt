package com.mohamed.sampleapp.di

import com.mohamed.sampleapp.data.repository.ProductsRepositoryImpl
import com.mohamed.sampleapp.domain.datasource.local.LocalDataSource
import com.mohamed.sampleapp.domain.datasource.remote.RemoteDataSource
import com.mohamed.sampleapp.domain.repository.ProductsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideProductsRepository(
        remoteDataSource: RemoteDataSource,
        localDataSource: LocalDataSource
    ): ProductsRepository = ProductsRepositoryImpl(remoteDataSource, localDataSource)
}