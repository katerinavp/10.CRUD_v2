package com.petukhova.mobile_auth.api

import android.content.Context
import android.util.Log
import com.petukhova.mobile_auth.API_SHARED_FILE
import com.petukhova.mobile_auth.AUTHENTICATED_SHARED_KEY
import com.petukhova.mobile_auth.BASE_URL
import com.petukhova.mobile_auth.dto.request.AuthRequestParams
import com.petukhova.mobile_auth.dto.Token
import com.petukhova.mobile_auth.dto.request.CreatePostRequest
import com.petukhova.mobile_auth.interceptor.InjectAuthTokenInterceptor
import com.petukhova.mobile_auth.model.PostModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Repository {

    // Ленивое создание Retrofit экземпляра
    private var retrofit: Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) //конвертация объектовв json
            .build()

    fun createRetrofitWithAuth(authToken: String) {
        val httpLoggerInterceptor = HttpLoggingInterceptor()
        // Указываем, что хотим логировать тело запроса.
        httpLoggerInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
            .addInterceptor(InjectAuthTokenInterceptor({ authToken }))
            .addInterceptor(httpLoggerInterceptor)
            .build()
        retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        //создаем API на основе нового retrofit-клиента
        api = retrofit.create(API::class.java)

    }

    //Ленивое создание API
    private var api: API = retrofit.create(API::class.java)// передаем интерфейс , берем его класс


    suspend fun authenticate(login: String, password: String): Response<Token> {

        Log.i("Auth go", "$login , $password")
        return api.authenticate(AuthRequestParams(login, password))
    }

    suspend fun registration(login: String, password: String) =
        api.registration(AuthRequestParams(login, password))

    suspend fun createPost(content: String): Response<Void> = api.createPost(
        CreatePostRequest(content = content)
    )

    suspend fun getPosts(): Response<List<PostModel>> = api.getPosts()
//    Log.i("getPosts", "$")
}