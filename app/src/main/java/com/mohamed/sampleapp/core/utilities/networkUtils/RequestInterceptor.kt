package com.mohamed.sampleapp.core.utilities.networkUtils

import com.mohamed.sampleapp.core.Constant
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class RequestInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        val authToken = "Bearer "

        val newRequest = chain.request().newBuilder()
            .addHeader(Constant.Authorization, authToken)
            .addHeader(Constant.Accept, Constant.AcceptValue)
            .build()
        return chain.proceed(newRequest)
    }
}