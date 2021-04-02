package com.petukhova.mobile_auth.activity

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.petukhova.mobile_auth.R
import com.petukhova.mobile_auth.adapter.PostAdapter
import com.petukhova.mobile_auth.api.Repository
import com.petukhova.mobile_auth.model.PostModel
import kotlinx.android.synthetic.main.activity_feed.*
import kotlinx.coroutines.launch
import splitties.activities.start
import splitties.toast.toast


class FeedActivity : AppCompatActivity(), PostAdapter.OnLikeBtnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)
        btnAddNewPost.setOnClickListener { (start<CreateNewPostActivity>()) }
    }

    override fun onStart() {
        super.onStart()
        progressBar.isVisible = true
        lifecycleScope.launch {
                val result = Repository.getPosts()

                if (result.isSuccessful) {
                    progressBar.isInvisible = true
                    with(container) {
                        layoutManager = LinearLayoutManager(this@FeedActivity)
                        adapter = PostAdapter(
                            (result.body() ?: emptyList()) as MutableList<PostModel>
                        ).apply {
                            likeBtnClickListener = this@FeedActivity
                        }
                    }
                }

             else {

                toast(getString(R.string.error))
            }

        }
    }

        override fun onLikeBtnClicked(item: PostModel, position: Int) {

        }
    }

