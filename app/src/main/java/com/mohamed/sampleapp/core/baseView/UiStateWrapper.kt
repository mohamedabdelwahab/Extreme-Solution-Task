package com.mohamed.sampleapp.core.baseView

sealed class UiStateWrapper<out T> {
    object Init : UiStateWrapper<Nothing>()
    object Loading : UiStateWrapper<Nothing>()
    data class Success<out T>(val data: T? = null) : UiStateWrapper<T>()
    data class Error(val code: Int? = null, val message: String) : UiStateWrapper<Nothing>()
}