package com.mohamed.sampleapp.di

import android.content.Context
import androidx.room.Room
import com.mohamed.sampleapp.data.source.local.FavoritesRoomDB
import com.mohamed.sampleapp.data.source.local.ProductDAO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomDBModule {

    @Provides
    @Singleton
    fun provideFavoritesRoomDB(@ApplicationContext appContext: Context): FavoritesRoomDB =
        Room.databaseBuilder(
            appContext,
            FavoritesRoomDB::class.java,
            "favorites_database.db"
        ).fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun provideProductFavoriteDAO(favoritesRoomDB: FavoritesRoomDB): ProductDAO =
        favoritesRoomDB.productFavoriteDAO()
}