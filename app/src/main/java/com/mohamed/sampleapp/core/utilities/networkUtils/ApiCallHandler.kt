package com.mohamed.sampleapp.core.utilities.networkUtils

import com.google.gson.Gson
import retrofit2.Response
import java.net.HttpURLConnection
import java.net.UnknownHostException

suspend fun <R> apiCall(call: suspend () -> Response<R>): Resource<R> {
    try {
        val response = call()
        val responseCode = response.code()
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) return Resource.Success(body)
        }
        val errorStr = response.errorBody()?.string()
        var errorObjects: BasicError? = null
        if (!errorStr.isNullOrEmpty()) {
            errorObjects = Gson().fromJson(errorStr, BasicError::class.java)
        }
        return when (responseCode) {
            HttpURLConnection.HTTP_GATEWAY_TIMEOUT,
            HttpURLConnection.HTTP_CLIENT_TIMEOUT
            -> Resource.Error(Failure.NetworkConnection)
            HttpURLConnection.HTTP_UNAUTHORIZED -> Resource.Error(Failure.UnAuthorize)
            HttpURLConnection.HTTP_NOT_FOUND,
            HttpURLConnection.HTTP_FORBIDDEN, HttpURLConnection.HTTP_UNAVAILABLE, HttpURLConnection.HTTP_INTERNAL_ERROR -> Resource.Error(
                error = Failure.ServerError(errorObjects?.detail)
            )
            else -> Resource.Error(error = Failure.UnknownError(errorObjects?.detail))
        }

    } catch (e: Exception) {
        return when (e) {
            is UnknownHostException -> Resource.Error(error = Failure.NetworkConnection)
            else -> {
                Resource.Error(error = Failure.UnknownError(e.toString()))
            }
        }
    }
}

private data class BasicError(
    val status: Int,
    val title: String,
    val traceId: String,
    val detail: String,
    val type: String
)