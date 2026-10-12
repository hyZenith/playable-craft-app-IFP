// AuthModels.kt
// Matches POST /api/auth/teacher/login and GET /api/auth/me

package com.example.myapplication.data.model

sealed interface LoginRequest {
    val password: String
}

data class TeacherLoginRequest(
    val applicationId: String, // NOT email — 6-16 uppercase letters/numbers, given by the school
    override val password: String
) : LoginRequest

typealias ApplicationIdLoginRequest = TeacherLoginRequest

data class EmailLoginRequest(
    val email: String,
    override val password: String
) : LoginRequest

data class LoginResponse(
    val id: String,
    val role: String, // "TEACHER"
    val name: String,
    val email: String,
    val accessToken: String,
    val accessTokenExpiresIn: Int = 900, // seconds, currently 900 (15 min)
    val providerId: String,
    val providerKind: String, // "SCHOOL" | "TUTOR"
    val applicationId: String
)

data class MeResponse(
    val id: String,
    val role: String,
    val name: String,
    val email: String,
    val providerId: String? = null,
    val providerKind: String? = null
)
