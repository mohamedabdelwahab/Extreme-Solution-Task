package com.mohamed.sampleapp.data.source.remote.reposnse

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Entity(tableName = "products")
@Parcelize
data class Product(
    @PrimaryKey
    @ColumnInfo(name = "id")
    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("image")
    val image: String? = null,

    @field:SerializedName("price")
    val price: Float = 0f,

    @Embedded
    @field:SerializedName("rating")
    val rating: Rating? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("title")
    val title: String? = null,

    @field:SerializedName("category")
    val category: String? = null,
    var isSelected: Boolean = false,
    var quantiy: Int = 0
) : Parcelable

@Parcelize
data class Rating(

    @field:SerializedName("rate")
    val rate: Float? = null,

    @field:SerializedName("count")
    val count: Int? = null
) : Parcelable
