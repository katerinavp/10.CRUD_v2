package com.petukhova.mobile_auth.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.petukhova.mobile_auth.R
import com.petukhova.mobile_auth.model.PostModel
import kotlinx.android.synthetic.main.activity_create_repost.*
import kotlinx.android.synthetic.main.item_load_more.view.*
import kotlinx.android.synthetic.main.item_load_new.view.*
import kotlinx.android.synthetic.main.item_load_new.view.progressbar
import kotlinx.android.synthetic.main.item_post.view.*
import kotlinx.android.synthetic.main.item_post.view.imageLike
import kotlinx.android.synthetic.main.item_post.view.imageRepost
import kotlinx.android.synthetic.main.item_post.view.txtAuthor
import kotlinx.android.synthetic.main.item_post.view.txtContent
import kotlinx.android.synthetic.main.item_post.view.txtLikeCount
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import splitties.toast.toast


class PostAdapter(val list: MutableList<PostModel>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val ITEM_TYPE_POST = 1
    private val ITEM_TYPE_REPOST = 2
//    private val ITEM_FOOTER = 3 //кнопка "загрузить еще"
//    private val ITEM_HEADER = 4 //кнопка "загрузить новые"

    var likeBtnClickListener: OnLikeBtnClickListener? = null
    var repostsBtnClickListener: OnRepostsBtnClickListener? = null
    var loadMoreBtnClickListener: OnLoadMoreBtnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if (viewType == ITEM_TYPE_POST) {
            val postView = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
            PostViewHolder(this, postView)
//        } else if(viewType == ITEM_FOOTER) {
//            val postView = LayoutInflater.from(parent.context).inflate(R.layout.item_load_more, parent, false)
//            FooterViewHolder(this, postView)
//        } else if(viewType == ITEM_HEADER) {
//            val postView = LayoutInflater.from(parent.context).inflate(R.layout.item_load_new, parent, false)
//            HeaderViewHolder(this, postView)
        }else{
            val repostView = LayoutInflater.from(parent.context).inflate(R.layout.item_repost, parent, false)
            RepostViewHolder(this, repostView)
        }
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is PostViewHolder -> holder.bind(list[position])
            is RepostViewHolder -> holder.bind(list[position])
        }
    }

    override fun getItemViewType(position: Int): Int {

        return when {
//            position == 0 -> ITEM_FOOTER
//            position == 0 -> ITEM_TYPE_POST
            list[position].source == null ->ITEM_TYPE_POST
         else -> ITEM_TYPE_REPOST
    }
    }

    interface OnLikeBtnClickListener {
        fun onLikeBtnClicked(item: PostModel, position: Int)
    }

    interface OnRepostsBtnClickListener {
        fun onRepostsBtnClicked(
            item: PostModel,
            position: Int,
            it: String
        )
    }

    interface OnLoadMoreBtnClickListener {
        fun onLoadMoreBtnClickListener(last: Long, size: Int)
    }

    fun newRecentPosts(list: List<PostModel>) {
        this.list.clear()
        this.list.addAll(list)
    }

}

class RepostViewHolder(val adapter: PostAdapter, view: View) : RecyclerView.ViewHolder(view) {
    // ...
    fun bind(post: PostModel) {

    }
}

class PostViewHolder(val adapter: PostAdapter, view: View) : RecyclerView.ViewHolder(view) {
    init {
        with(itemView) {
            imageLike.setOnClickListener {
                val currentPosition = adapterPosition
                if (currentPosition != RecyclerView.NO_POSITION) {
                    val item = adapter.list[currentPosition]
                    if (item.likeActionPerforming) {
                        context.toast("Like is performing")
                    } else {
                        adapter.likeBtnClickListener?.onLikeBtnClicked(item, currentPosition)
                    }
                }
            }
            imageRepost.setOnClickListener {
                val currentPosition = adapterPosition
                if (currentPosition != RecyclerView.NO_POSITION) {
                    val item = adapter.list[adapterPosition]
                    if (item.repostedByMe) {
                        context.toast("Can't repost repost)")
                    } else {
                        showDialog(context) {
                            adapter.repostsBtnClickListener?.onRepostsBtnClicked(
                                item,
                                currentPosition,
                                it
                            )
                        }
                    }
                }
            }
            //..
        }
    }

    fun bind(post: PostModel) {
        with(itemView) {
            txtAuthor.text = post.ownerName
            txtContent.text = post.content
            txtLikeCount.text = post.likes.toString()
            txtRepostCount.text = post.reposts.toString()

            if (post.likeActionPerforming) {
                imageLike.setImageResource(R.drawable.ic_like_black)
            } else if (post.likedByMe) {
                imageLike.setImageResource(R.drawable.ic_thumb_up)
                txtLikeCount.setTextColor(ContextCompat.getColor(context, R.color.blue))
            } else {
                imageLike.setImageResource(R.drawable.ic_like_black)
                txtLikeCount.setTextColor(ContextCompat.getColor(context, R.color.grey))
            }

            if (post.repostActionPerforming) {
                imageRepost.setImageResource(R.drawable.ic_repost_black)
                // imageShare.setImageResource(R.drawable.ic_refresh)
            } else if (post.repostedByMe) {
                imageRepost.setImageResource(R.drawable.ic_repost_blue)
                txtRepostCount.setTextColor(ContextCompat.getColor(context, R.color.blue))
            } else {
                imageRepost.setImageResource(R.drawable.ic_repost_black)
                txtRepostCount.setTextColor(ContextCompat.getColor(context, R.color.grey))
            }
        }
    }

    fun showDialog(context: Context, createBtnClicked: (content: String) -> Unit) {
        val dialog = AlertDialog.Builder(context)
            .setView(R.layout.activity_create_repost)
            .show()
        dialog.btnCreateRepost.setOnClickListener {
            createBtnClicked(dialog.txtInputBodyRepost.text.toString())
            dialog.dismiss()
        }
    }
}

//    class HeaderViewHolder(val adapter: PostAdapter, view: View) : RecyclerView.ViewHolder(view) {
//        init {
//            with(itemView) {
//                // Слушатель на кнопку
//                loadNewBtn.setOnClickListener {
//                    // делаем кнопку неактивной пока идет запрос
//                    loadNewBtn.isEnabled = false
//                    // над кнопкой покажем progressBar
//                    progressbar.visibility = View.VISIBLE
//                    GlobalScope.launch(Dispatchers.Main) {
//                        // запрашиваем все посты после нашего первого поста
//                        // (он же самый последний)
//                        val response = Repository.getPostsAfter(adapter.list[0].id)
//                        // восстанавливаем справедливость
//                        progressbar.visibility = View.INVISIBLE
//                        loadNewBtn.isEnabled = true
//                        if (response.isSuccessful) {
//                            // Если все успешно, то новые элементы добавляем в начало
//                            // нашего списка.
//                            val newItems = response.body()!!
//                            adapter.list.addAll(0, newItems)
//                            // Оповещаем адаптер о новых элементах
//                            adapter.notifyItemRangeInserted(0, newItems.size)
//                        } else {
//                            context.toast("Error occured")
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    class FooterViewHolder(val adapter: PostAdapter, view: View) : RecyclerView.ViewHolder(view) {
//        init {
//            with(itemView) {
//                // Слушатель на кнопку
//                loadMoreBtn.setOnClickListener {
//                    // делаем кнопку неактивной пока идет запрос
//                    loadMoreBtn.isEnabled = false
//                    // над кнопкой покажем progressBar
//                    progressbar.visibility = View.VISIBLE
//                    GlobalScope.launch(Dispatchers.Main) {
//                        // запрашиваем все посты после нашего первого поста
//                        // (он же самый последний)
//                        val response = Repository.getPostsBefore(adapter.list[0].id)
//                        // восстанавливаем справедливость
//                        progressbar.visibility = View.INVISIBLE
//                        loadMoreBtn.isEnabled = true
//                        if (response.isSuccessful) {
//                            // Если все успешно, то новые элементы добавляем в начало
//                            // нашего списка.
//                            val newItems = response.body()!!
//                            adapter.list.addAll(0, newItems)
//                            // Оповещаем адаптер о новых элементах
//                            adapter.notifyItemRangeInserted(0, newItems.size)
//                        } else {
//                            context.toast("Error occured")
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//
