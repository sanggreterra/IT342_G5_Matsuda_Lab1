package com.it342.timesheets.data

import com.google.gson.annotations.SerializedName

/** Matches backend UserResponse */
data class UserResponse(
    @SerializedName("userId") val userId: Int,
    @SerializedName("username") val username: String,
    @SerializedName("email") val email: String
)

/** Matches backend AuthResponse */
data class AuthResponse(
    @SerializedName("token") val token: String,
    @SerializedName("user") val user: UserResponse
)

/** Matches backend LoginRequest */
data class LoginRequest(
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String
)

/** Matches backend RegisterRequest */
data class RegisterRequest(
    @SerializedName("username") val username: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

/** Backend error body: { "error": "message" } */
data class ErrorResponse(
    @SerializedName("error") val error: String? = null,
    @SerializedName("message") val message: String? = null
)
