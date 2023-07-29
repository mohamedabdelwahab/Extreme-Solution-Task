package com.mohamed.sampleapp.core.utilities.networkUtils

sealed class Resource<out T> {
    object Loading : Resource<Nothing>()
    data class Success<T>(val data: T) : Resource<T>()
    data class Error(val error: Failure) : Resource<Nothing>()
}