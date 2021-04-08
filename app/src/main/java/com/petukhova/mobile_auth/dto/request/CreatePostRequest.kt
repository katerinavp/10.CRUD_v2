package com.petukhova.mobile_auth.dto.request

// Данные для создания поста (для новых постов id=0), если id есть , то пост меняем
data class CreatePostRequest(val id: Long = 0, val content: String)