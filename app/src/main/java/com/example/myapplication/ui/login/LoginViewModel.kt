package com.example.myapplication.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.ApplicationIdLoginRequest
import com.example.myapplication.data.model.EmailLoginRequest
import com.example.myapplication.data.model.LoginRequest
import com.example.myapplication.data.model.LoginResponse
import com.example.myapplication.data.repo.AuthRepository
import com.example.myapplication.data.repo.AuthResult
import kotlinx.coroutines.launch

enum class SignInRole(val label: String) {
    SCHOOL("School"), TUTOR("Tutor"), TEACHER("Teacher"), STUDENT("Student");

    val usesApplicationId: Boolean get() = this == TUTOR || this == TEACHER
}

data class LoginUiState(
    val selectedRole: SignInRole = SignInRole.TEACHER,
    val identifier: String = "",          // email, or Application ID for Teacher/Tutor
    val password: String = "",
    val keepSignedIn: Boolean = true,
    val passwordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val loggedInUser: LoginResponse? = null
)

class LoginViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    var uiState by mutableStateOf(LoginUiState())
        private set

    // ── Field updates ────────────────────────────────────────────

    fun onRoleSelected(role: SignInRole) {
        uiState = uiState.copy(
            selectedRole = role,
            identifier = if (role.usesApplicationId != uiState.selectedRole.usesApplicationId) "" else uiState.identifier,
            errorMessage = null
        )
    }

    fun onIdentifierChange(value: String) {
        uiState = uiState.copy(identifier = value, errorMessage = null)
    }

    fun onPasswordChange(value: String) {
        uiState = uiState.copy(password = value, errorMessage = null)
    }

    fun onKeepSignedInToggle(checked: Boolean) {
        uiState = uiState.copy(keepSignedIn = checked)
    }

    fun onTogglePasswordVisibility() {
        uiState = uiState.copy(passwordVisible = !uiState.passwordVisible)
    }

    // ── Login action ─────────────────────────────────────────────

    fun login() {
        val role = uiState.selectedRole
        val id = uiState.identifier.trim()

        if (id.isBlank() || uiState.password.isBlank()) {
            uiState = uiState.copy(
                errorMessage = if (role.usesApplicationId) "Application ID and password are required."
                               else "Email and password are required."
            )
            return
        }
        if (!role.usesApplicationId && !android.util.Patterns.EMAIL_ADDRESS.matcher(id).matches()) {
            uiState = uiState.copy(errorMessage = "Enter a valid email address.")
            return
        }

        uiState = uiState.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            val request: LoginRequest =
                if (role.usesApplicationId) ApplicationIdLoginRequest(id.uppercase(), uiState.password)
                else EmailLoginRequest(id, uiState.password)

            uiState = when (val result = authRepository.login(role, request)) {
                is AuthResult.Success -> uiState.copy(isLoading = false, loggedInUser = result.response)
                is AuthResult.Error -> uiState.copy(isLoading = false, errorMessage = result.message)
            }
        }
    }

    /** Reset after the caller has consumed the login success. */
    fun consumeLoginSuccess() {
        uiState = uiState.copy(loggedInUser = null)
    }
}
