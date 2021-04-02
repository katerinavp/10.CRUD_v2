package com.petukhova.mobile_auth.api


import com.petukhova.mobile_auth.dto.request.AuthRequestParams
import com.petukhova.mobile_auth.dto.Token
import com.petukhova.mobile_auth.dto.request.CreatePostRequest
import com.petukhova.mobile_auth.dto.request.RepostRequest
import com.petukhova.mobile_auth.model.PostModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface API {
    // URL запроса (без учета основного адреса)
    @POST("api/v1/authentication")
    suspend fun authenticate(@Body authRequestParams: AuthRequestParams): Response<Token>

    @POST("api/v1/registration")
    suspend fun registration(@Body authRequestParams: AuthRequestParams): Response<Token>

    @POST("api/v1/posts")
    suspend fun createPost(@Body createPostRequest: CreatePostRequest): Response<Void>

    @POST("api/v1/reposts")
    suspend fun repost(@Body repostRequest: RepostRequest): Response<Void>

    @GET("api/v1/posts")
    suspend fun getPosts(): Response<List<PostModel>>
}
