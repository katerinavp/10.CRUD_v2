package com.petukhova.mobile_auth.activity

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.petukhova.mobile_auth.R
import com.petukhova.mobile_auth.check.Check
import com.petukhova.mobile_auth.api.Repository
import kotlinx.android.synthetic.main.activity_create_post.*
import kotlinx.coroutines.launch
import splitties.toast.toast
import java.io.IOException

class CreateNewPostActivity : AppCompatActivity() {

    private var dialog: ProgressDialog? = null
    private val check = Check()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)

        btnCreatePost.setOnClickListener { createNewPost() }
    }

    private fun createNewPost() {

        val txtInputBodyPost: String = txtInputBodyPost.text.toString()
        if (check.checkPostIsNotEmpty(txtInputBodyPost)) {
            toast(getString(R.string.enter_post))
        } else {
            lifecycleScope.launch() {
                try {
                    val result = Repository.createPost(txtInputBodyPost)
                    //binding.progressBar.isInvisible = true
                    if (result.isSuccessful) {
                        // обрабатываем успешное создание поста
                        handleSuccessfullResult()

                    }
                    else {
                     //   обрабатываем ошибку

                        handleFailedResult()
                    }
                } catch (e: IOException) {
                    Log.i("ошибка catch", "$e")
                    // обрабатываем ошибку
                    handleFailedResult()
                } finally {
                    // закрываем диалог
                    dialog?.dismiss()
                }


            }
        }
    }

    private fun handleSuccessfullResult() {
        toast(getString(R.string.post_created_successfull))
        finish()
    }

    private fun handleFailedResult() {
        //Log.i("ошибка catch", "$e")
        toast(getString(R.string.post_error))
    }

}
