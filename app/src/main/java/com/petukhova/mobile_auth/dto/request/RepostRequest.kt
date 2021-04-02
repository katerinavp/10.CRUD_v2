package com.petukhova.mobile_auth.dto.request

data class RepostRequest(
    val id: Long = 0,
    val parentPostId: Long,
    val authorId: Long,
    val content: String
)