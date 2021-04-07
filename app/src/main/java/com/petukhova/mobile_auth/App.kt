//package com.petukhova.mobile_auth
//
//import android.app.Application
//import android.content.Context
//import com.petukhova.mobile_auth.api.API
//import com.petukhova.mobile_auth.api.Repository
//
//import com.petukhova.mobile_auth.interceptor.InjectAuthTokenInterceptor
//import okhttp3.OkHttpClient
//import okhttp3.logging.HttpLoggingInterceptor
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//
//class App : Application() {
//    lateinit var authTokenInterceptor: InjectAuthTokenInterceptor
//
//    companion object {
//        //         lateinit var authTokenInterceptor: InjectAuthTokenInterceptor
//        lateinit var repository: Repository
//
//    }
//
//    private var retrofit: Retrofit =
//        Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//
//    // получаем токен из sharedpref
//    override fun onCreate() {
//        super.onCreate()
//
////        val httpLoggerInterceptor = HttpLoggingInterceptor()
////        // Указываем, что хотим логировать тело запроса.
////        httpLoggerInterceptor.level = HttpLoggingInterceptor.Level.BODY
////        val client = OkHttpClient.Builder()
////            .addInterceptor(InjectAuthTokenInterceptor {
////                getSharedPreferences(API_SHARED_FILE, Context.MODE_PRIVATE).getString(
////                    AUTHENTICATED_SHARED_KEY, null
////                )
////            })
////            .addInterceptor(httpLoggerInterceptor)
////            .build()
////
////        //создаем объект для запроса к серверу
////        val retrofit = Retrofit.Builder()
////            .client(client)
////            .baseUrl(BASE_URL)
////            .addConverterFactory(GsonConverterFactory.create())
////            .build()
////        //создаем API на основе нового retrofit-клиента
////        val api =
////            retrofit.create(API::class.java) //указываем наш класс интерфейса с запросами к сайту
////        repository = Repositoryimp(api)
////    }
////}
//
////
////        authTokenInterceptor = InjectAuthTokenInterceptor {
////            getSharedPreferences(API_SHARED_FILE, MODE_PRIVATE).getString(
////                AUTHENTICATED_SHARED_KEY, ""
////            )
////        }
//    }
//}