package com.petukhova.mobile_auth

import android.app.Application
import com.petukhova.mobile_auth.interceptor.InjectAuthTokenInterceptor

class App : Application() {

    companion object {
        lateinit var authTokenInterceptor: InjectAuthTokenInterceptor
    }
    // получаем токен из sharedpref
    override fun onCreate() {
        super.onCreate()

        authTokenInterceptor = InjectAuthTokenInterceptor{
            getSharedPreferences(API_SHARED_FILE, MODE_PRIVATE).getString(
                AUTHENTICATED_SHARED_KEY, ""
            )
        }
    }
}