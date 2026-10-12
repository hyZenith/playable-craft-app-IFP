package com.example.myapplication.ui.signup

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// Teacher is not here on purpose: teacher accounts are issued by the school.
enum class SignUpRole(val label: String) {
    SCHOOL("School"), TUTOR("Tutor"), STUDENT("Student")
}

val institutionTypes = listOf(
    "Primary School",
    "Middle School",
    "Secondary School",
    "Senior Secondary School",
    "Coaching / Training Institute"
)

data class SignUpUiState(
    val role: SignUpRole = SignUpRole.SCHOOL,
    val name: String = "",
    val email: String = "",
    val emailVerified: Boolean = false,
    val emailVerifying: Boolean = false,
    val phone: String = "",
    val phoneVerified: Boolean = false,
    val phoneVerifying: Boolean = false,
    val password: String = "",
    val passwordVisible: Boolean = false,
    val institutionType: String = institutionTypes[3],
    val udiseCode: String = "",
    val certificateName: String? = null,
    val certificateUri: String? = null,
    val acceptedTerms: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val signedUp: Boolean = false
) {
    val isEmailValid get() = Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()
    val isPhoneValid get() = phone.length == 10
    val isSchool get() = role == SignUpRole.SCHOOL
}

data class SignUpRequest(
    val role: String,
    val name: String,
    val email: String,
    val phone: String,
    val password: String,
    val institutionType: String?,
    val udiseCode: String?,
    val certificateUri: String?
)

sealed interface SignUpResult {
    data object Success : SignUpResult
    data class Error(val message: String) : SignUpResult
}

interface SignUpRepository {
    suspend fun verifyEmail(email: String): Boolean
    suspend fun verifyPhone(phone: String): Boolean
    suspend fun signUp(request: SignUpRequest): SignUpResult
}

/** Fake for local development. Replace with the real API (OTP flow) later. */
class FakeSignUpRepository : SignUpRepository {
    override suspend fun verifyEmail(email: String): Boolean { delay(1_000); return true }
    override suspend fun verifyPhone(phone: String): Boolean { delay(1_000); return true }
    override suspend fun signUp(request: SignUpRequest): SignUpResult {
        delay(1_500)
        return SignUpResult.Success
    }
}

class SignUpViewModel(
    private val repository: SignUpRepository
) : ViewModel() {

    var uiState by mutableStateOf(SignUpUiState())
        private set

    private fun update(block: SignUpUiState.() -> SignUpUiState) {
        uiState = uiState.block().copy(errorMessage = null)
    }

    fun onRoleSelected(role: SignUpRole) = update { copy(role = role) }
    fun onNameChange(v: String) = update { copy(name = v) }
    fun onEmailChange(v: String) = update { copy(email = v, emailVerified = false) }
    fun onPhoneChange(v: String) =
        update { copy(phone = v.filter(Char::isDigit).take(10), phoneVerified = false) }
    fun onPasswordChange(v: String) = update { copy(password = v) }
    fun onTogglePasswordVisibility() = update { copy(passwordVisible = !passwordVisible) }
    fun onInstitutionTypeChange(v: String) = update { copy(institutionType = v) }
    fun onUdiseChange(v: String) = update { copy(udiseCode = v.filter(Char::isDigit).take(11)) }
    fun onTermsToggle(v: Boolean) = update { copy(acceptedTerms = v) }
    fun onCertificatePicked(uri: String, name: String) =
        update { copy(certificateUri = uri, certificateName = name) }

    fun onVerifyEmail() {
        if (!uiState.isEmailValid) return
        uiState = uiState.copy(emailVerifying = true)
        viewModelScope.launch {
            val ok = repository.verifyEmail(uiState.email.trim())
            uiState = uiState.copy(
                emailVerifying = false,
                emailVerified = ok,
                errorMessage = if (ok) null else "Could not verify email. Try again."
            )
        }
    }

    fun onVerifyPhone() {
        if (!uiState.isPhoneValid) return
        uiState = uiState.copy(phoneVerifying = true)
        viewModelScope.launch {
            val ok = repository.verifyPhone(uiState.phone)
            uiState = uiState.copy(
                phoneVerifying = false,
                phoneVerified = ok,
                errorMessage = if (ok) null else "Could not verify phone number. Try again."
            )
        }
    }

    fun onSubmit() {
        val s = uiState
        val error = when {
            s.name.isBlank() ->
                if (s.isSchool) "Enter your school / institution name." else "Enter your full name."
            !s.isEmailValid -> "Enter a valid email address."
            !s.emailVerified -> "Please verify your email."
            !s.isPhoneValid -> "Enter a 10-digit phone number."
            !s.phoneVerified -> "Please verify your phone number."
            s.password.length < 8 -> "Password must be at least 8 characters."
            s.isSchool && s.udiseCode.length != 11 -> "UDISE code must be 11 digits."
            s.isSchool && s.certificateUri == null -> "Upload your affiliation / recognition certificate."
            !s.acceptedTerms -> "Please accept the Terms and Privacy Policy."
            else -> null
        }
        if (error != null) {
            uiState = s.copy(errorMessage = error)
            return
        }

        uiState = s.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            val result = repository.signUp(
                SignUpRequest(
                    role = s.role.name,
                    name = s.name.trim(),
                    email = s.email.trim(),
                    phone = s.phone,
                    password = s.password,
                    institutionType = if (s.isSchool) s.institutionType else null,
                    udiseCode = if (s.isSchool) s.udiseCode else null,
                    certificateUri = if (s.isSchool) s.certificateUri else null
                )
            )
            uiState = when (result) {
                SignUpResult.Success -> uiState.copy(isLoading = false, signedUp = true)
                is SignUpResult.Error -> uiState.copy(isLoading = false, errorMessage = result.message)
            }
        }
    }

    fun consumeSignedUp() { uiState = uiState.copy(signedUp = false) }
}
