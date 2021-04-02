package com.petukhova.mobile_auth.api

import android.util.Log
import com.petukhova.mobile_auth.dto.request.AuthRequestParams
import com.petukhova.mobile_auth.dto.Token
import com.petukhova.mobile_auth.dto.request.CreatePostRequest
import com.petukhova.mobile_auth.model.PostModel
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Repository {
    // Ленивое создание Retrofit экземпляра
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://katerinavpbackendfornetwork.herokuapp.com")
            .addConverterFactory(GsonConverterFactory.create()) //конвертация объектовв json
            .build()
    }
    // Ленивое создание API
    private val api: API by lazy {
        retrofit.create(
            API::class.java// передаем интерфейс , берем его класс
        )
    }
    suspend fun authenticate(login: String, password: String): Response<Token> {
        Log.i("Auth go", "$login , $password")
        return  api.authenticate(AuthRequestParams(login, password))
    }

    suspend fun registration(login: String, password: String) =
        api.registration(AuthRequestParams(login, password))

    suspend fun createPost(content: String) = api.createPost(
        CreatePostRequest(content = content)
    )
    suspend fun getPosts() = api.getPosts()



}