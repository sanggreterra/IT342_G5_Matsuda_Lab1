package com.it342.timesheets.data

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface TimesheetsApi {

    @POST("api/auth/register")
    suspend fun register(@Body body: RegisterRequest): Response<AuthResponse>

    @POST("api/auth/login")
    suspend fun login(@Body body: LoginRequest): Response<AuthResponse>

    @POST("api/auth/logout")
    suspend fun logout(@Header("Authorization") authorization: String): Response<Unit>

    @GET("api/user/me")
    suspend fun getMe(@Header("Authorization") authorization: String): Response<UserResponse>
}
