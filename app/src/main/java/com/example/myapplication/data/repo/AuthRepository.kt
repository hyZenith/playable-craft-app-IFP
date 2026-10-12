package com.example.myapplication.data.repo

import com.example.myapplication.data.model.EmailLoginRequest
import com.example.myapplication.data.model.LoginRequest
import com.example.myapplication.data.model.LoginResponse
import com.example.myapplication.data.model.TeacherLoginRequest
import com.example.myapplication.ui.login.SignInRole
import kotlinx.coroutines.delay

/** Sealed result wrapper for auth operations. */
sealed interface AuthResult {
    data class Success(val response: LoginResponse) : AuthResult
    data class Error(val message: String) : AuthResult
}

interface AuthRepository {
    suspend fun login(role: SignInRole, request: LoginRequest): AuthResult
}

class FakeAuthRepository : AuthRepository {
    override suspend fun login(role: SignInRole, request: LoginRequest): AuthResult {
        delay(1_500)
        val id = when (request) {
            is EmailLoginRequest -> request.email
            is TeacherLoginRequest -> request.applicationId
        }
        if (id.equals("error", ignoreCase = true)) {
            return AuthResult.Error("Invalid credentials.")
        }
        val isTeacher = role == SignInRole.TEACHER
        return AuthResult.Success(
            LoginResponse(
                id = if (isTeacher) "tch_fake_001" else "usr_fake_001",
                role = if (isTeacher) "TEACHER" else role.name,
                name = if (isTeacher) "Sarah Jenkins" else "Jane Doe",
                email = (request as? EmailLoginRequest)?.email ?: "teacher@school.edu",
                accessToken = "fake-jwt-token",
                accessTokenExpiresIn = 900,
                providerId = "prv_school_001",
                providerKind = if (role == SignInRole.TUTOR) "TUTOR" else "SCHOOL",
                applicationId = (request as? TeacherLoginRequest)?.applicationId ?: "SCH-TEACH-9001"
            )
        )
    }
}
