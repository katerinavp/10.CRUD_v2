package com.petukhova.mobile_auth.model

data class PostModel(
    val id: Long,
    val source: PostModel? = null, // ссылка на исходный пост
    val ownerId: Long,
    val ownerName: String,
    val created: Int,
    var content: String? = null,
    var likes: Int = 0,
    var likedByMe: Boolean = false,
    var reposts: Int = 0,
    var repostedByMe: Boolean = false,
    val link: String? = null,
    val type: PostType = PostType.POST,
    val attachment: AttachmentModel?
){
    var likeActionPerforming = false
    var repostActionPerforming = false

    // После получения поста обратно обновляем данные, которые можно обновить
    fun updatePost(updatedModel: PostModel) {
        if (id != updatedModel.id) throw IllegalAccessException("Ids are different")
        likes = updatedModel.likes
        likedByMe = updatedModel.likedByMe
        content = updatedModel.content
        reposts = updatedModel.reposts
        repostedByMe = updatedModel.repostedByMe
    }
}