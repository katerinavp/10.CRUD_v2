package com.petukhova.mobile_auth.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.content.edit
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.petukhova.mobile_auth.*
import com.petukhova.mobile_auth.check.Check
import com.petukhova.mobile_auth.databinding.ActivityAuthBinding
import com.petukhova.mobile_auth.api.Repository
import isValid
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import splitties.activities.start
import splitties.toast.toast

class AuthActivity :
    AppCompatActivity() { // можно здесь указать ссылку на xml файл вместо setContentView
    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityAuthBinding.inflate(LayoutInflater.from(this))
    }

    var authenticated = false

    val check = Check()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        if (isAuthenticated()) {
            val token = getSharedPreferences(API_SHARED_FILE, Context.MODE_PRIVATE).getString(
                AUTHENTICATED_SHARED_KEY, ""
            )
            // Пересоздаем retrofit-клиент с токеном аутентификации
            Repository.createRetrofitWithAuth(token!!)
            goFeedActivity()
        }

        binding.btnLog.setOnClickListener { auth() }
        binding.btnReg.setOnClickListener { goRegActivity() }

    }

    override fun onStart() {
        super.onStart()
        if (isAuthenticated()) {
            val token = getSharedPreferences(API_SHARED_FILE, Context.MODE_PRIVATE).getString(
                AUTHENTICATED_SHARED_KEY, ""
            )
            Repository.createRetrofitWithAuth(token!!)
            start<FeedActivity>()
            finish()
        }
    }

    private fun auth() {
        val login: String = binding.textInputLogin.text.toString()
        val password: String = binding.textInputPassword.text.toString()
        if (!check.checktextInputAuth(login, password)) { //  проверка на пустые поля ввода
            toast(R.string.enter_login_password)
        } else {
            if (!isValid(password)) {
                binding.textInputPassword.error = getString(R.string.check_password_length)
            } else {
                binding.progressBar.isVisible =
                    true // если проверка прошла успешно запускаем прогерссбар и корутину для отправки post запроса аутентификации на сервер
                lifecycleScope.launch {

                    delay(5000)
                    try {
                        val token = Repository.authenticate(login, password)
                        binding.progressBar.isInvisible = true
                        if (token.isSuccessful) {
                            authenticated = true

                            setUserAuth(requireNotNull(token.body()).token)
                            Repository.createRetrofitWithAuth(token.body()!!.token)
                            toast(R.string.success_auth)
                            goFeedActivity()

                        } else {
                            toast(R.string.Unsuccess_auth)
                        }
                    } catch (e: Exception) {
                        toast(R.string.error_server)

                    }
                }
            }
        }
    }

    private fun goRegActivity() {
        start<RegistrationActivity>()
        finish()

    }

    private fun isAuthenticated() =
        getSharedPreferences(API_SHARED_FILE, Context.MODE_PRIVATE).getString(
            AUTHENTICATED_SHARED_KEY, ""
        )?.isNotEmpty() ?: false

    private fun setUserAuth(token: String) =
        getSharedPreferences(API_SHARED_FILE, Context.MODE_PRIVATE)
            //extension можно использовать, не нужно вызывать apply
            .edit { putString(AUTHENTICATED_SHARED_KEY, token) }



    private fun goFeedActivity() {

        start<FeedActivity>()
        finish()

    }
}





