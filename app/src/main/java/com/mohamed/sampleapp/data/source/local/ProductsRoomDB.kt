package com.mohamed.sampleapp.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mohamed.sampleapp.data.source.remote.reposnse.Product

@Database(entities = [Product::class], version = 1, exportSchema = false)
abstract class ProductsRoomDB : RoomDatabase() {
    abstract fun productDAO(): ProductDAO
}