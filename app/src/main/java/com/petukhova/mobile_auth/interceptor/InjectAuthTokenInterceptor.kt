package com.petukhova.mobile_auth.interceptor

import com.petukhova.mobile_auth.AUTH_TOKEN_HEADER
import okhttp3.Interceptor
import okhttp3.Response

class InjectAuthTokenInterceptor(private val authToken: () -> String?) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
// Оригинальный запрос
        val originalRequest = chain.request()
// Создаем новый запрос на основе оригинального, добавляя в него токен
        val requestWithToken = originalRequest.newBuilder()
            .header(AUTH_TOKEN_HEADER, "Bearer  ${authToken()}")
            .build()
// Возвращаем (передаем дальше) новый запрос с токеном
// вместо оригинального
        return chain.proceed(requestWithToken)
    }
}