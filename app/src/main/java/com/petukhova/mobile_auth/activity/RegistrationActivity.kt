package com.petukhova.mobile_auth.activity

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.petukhova.mobile_auth.API_SHARED_FILE
import com.petukhova.mobile_auth.AUTHENTICATED_SHARED_KEY
import com.petukhova.mobile_auth.check.Check
import com.petukhova.mobile_auth.R
import com.petukhova.mobile_auth.api.API
import com.petukhova.mobile_auth.api.Repository

import com.petukhova.mobile_auth.api.Repository.registration
import com.petukhova.mobile_auth.dto.Token
import com.petukhova.mobile_auth.databinding.ActivityRegistrationBinding
import isValid
import kotlinx.coroutines.launch
import retrofit2.Response
import splitties.activities.start
import splitties.toast.toast

class RegistrationActivity : AppCompatActivity() {

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityRegistrationBinding.inflate(LayoutInflater.from(this))
    }
    val check = Check()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnSave.setOnClickListener {registration() }
    }

    private fun registration() {
        val userName: String = binding.textInputUserName.text.toString()
        val password: String = binding.textInputPassword.text.toString()
        val passwordRepeat: String = binding.textInputPasswordRepeat.text.toString()

        if (!check.checktextInputReg(
                userName,
                password,
                passwordRepeat
            )
        ) {//проверка на пустые поля ввода
            toast(R.string.enter_login_password)
        } else {
            if (!check.checkPassword(password, passwordRepeat)) { //проверка совпадают ли пароли
                toast(R.string.password_incorrect)
            } else {
                if (!isValid(password)) {
                    binding.textInputPassword.error = getString(R.string.check_password_length)
                    binding.textInputPasswordRepeat.error = getString(R.string.check_password_length)
                } else {
                    binding.progressBar.isVisible = true
                    lifecycleScope.launch { // если поля ввода заполнены и пароли совпадают запускаем корутину и выполняем post запрос регистрации на сервер
                        try {
                            val token = Repository.registration(userName, password)
                            //val token: Response<Token> = Repository.registration(userName, password)
                            binding.progressBar.isInvisible = true
                            if (token.isSuccessful) {
                                //с помощью splitties можно сократить toast
                                toast(R.string.success_reg)
                                setUserAuth(token.body()!!.token)
                                goMainActivity()

                            } else {

                                toast(R.string.unsuccess_reg)
                            }
                        } catch (e: Exception) {
                            toast(R.string.error_server)
                        }
                    }
                }
            }
        }
    }


    private fun goMainActivity() {
        start<AuthActivity>()
        finish()
    }

    private fun setUserAuth(token: String) =
        getSharedPreferences(API_SHARED_FILE, Context.MODE_PRIVATE)
            .edit()
            .putString(AUTHENTICATED_SHARED_KEY, token)
            .commit()
}