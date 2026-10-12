package com.example.myapplication.ui.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.UserProfile
import com.example.myapplication.data.repo.ProfileRepository
import kotlinx.coroutines.launch

/**
 * Immutable UI state for the Profile management screen.
 *
 * @property profile Current loaded user profile.
 * @property isLoading Indicates whether profile details are being loaded.
 * @property isEditing Whether the edit profile dialog is displayed.
 * @property editName Buffer for editing the user's name.
 * @property editEmail Buffer for editing the user's email.
 * @property isSavingProfile Indicates an active update network request.
 * @property feedbackText Current text in the write feedback input field.
 * @property isSubmittingFeedback Indicates an active feedback submission.
 * @property feedbackSuccessMessage Non-null when feedback has been successfully submitted.
 * @property showSignOutDialog Whether the sign out confirmation alert is displayed.
 * @property errorMessage Non-null if an error occurred.
 */
data class ProfileUiState(
    val profile: UserProfile? = null,
    val isLoading: Boolean = true,
    val isEditing: Boolean = false,
    val editName: String = "",
    val editEmail: String = "",
    val isSavingProfile: Boolean = false,
    val feedbackText: String = "",
    val isSubmittingFeedback: Boolean = false,
    val feedbackSuccessMessage: String? = null,
    val showSignOutDialog: Boolean = false,
    val errorMessage: String? = null
)

/**
 * ViewModel orchestrating profile details, account edits, feedback submissions, and session logout.
 */
class ProfileViewModel(
    private val repository: ProfileRepository
) : ViewModel() {

    var uiState by mutableStateOf(ProfileUiState())
        private set

    init {
        loadProfile()
    }

    /**
     * Loads the user's profile details.
     */
    fun loadProfile() {
        uiState = uiState.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            val result = repository.getProfile()
            if (result.isSuccess) {
                uiState = uiState.copy(
                    isLoading = false,
                    profile = result.getOrNull()
                )
            } else {
                uiState = uiState.copy(
                    isLoading = false,
                    errorMessage = "Unable to load profile information. Please try again."
                )
            }
        }
    }

    /**
     * Opens the edit profile dialog with current values.
     */
    fun startEditing() {
        val current = uiState.profile ?: return
        uiState = uiState.copy(
            isEditing = true,
            editName = current.fullName,
            editEmail = current.email
        )
    }

    /**
     * Closes the edit profile dialog without saving changes.
     */
    fun dismissEditing() {
        uiState = uiState.copy(isEditing = false)
    }

    /**
     * Updates the name input buffer.
     */
    fun onEditNameChange(name: String) {
        uiState = uiState.copy(editName = name)
    }

    /**
     * Updates the email input buffer.
     */
    fun onEditEmailChange(email: String) {
        uiState = uiState.copy(editEmail = email)
    }

    /**
     * Persists updated profile credentials.
     */
    fun saveProfile() {
        val name = uiState.editName.trim()
        val email = uiState.editEmail.trim()
        if (name.isEmpty() || email.isEmpty()) return

        uiState = uiState.copy(isSavingProfile = true)
        viewModelScope.launch {
            val result = repository.updateProfile(name, email)
            if (result.isSuccess) {
                uiState = uiState.copy(
                    isSavingProfile = false,
                    isEditing = false,
                    profile = result.getOrNull()
                )
            } else {
                uiState = uiState.copy(
                    isSavingProfile = false,
                    errorMessage = "Failed to update profile. Please try again."
                )
            }
        }
    }

    /**
     * Updates the feedback input field content.
     */
    fun onFeedbackChange(text: String) {
        uiState = uiState.copy(feedbackText = text)
    }

    /**
     * Submits written feedback to the backend.
     */
    fun submitFeedback() {
        val text = uiState.feedbackText.trim()
        if (text.isEmpty()) return

        uiState = uiState.copy(isSubmittingFeedback = true)
        viewModelScope.launch {
            val result = repository.submitFeedback(text)
            if (result.isSuccess) {
                uiState = uiState.copy(
                    isSubmittingFeedback = false,
                    feedbackText = "",
                    feedbackSuccessMessage = "Thank you! Your feedback has been received."
                )
            } else {
                uiState = uiState.copy(
                    isSubmittingFeedback = false,
                    errorMessage = "Failed to send feedback. Please try again."
                )
            }
        }
    }

    /**
     * Clears the feedback confirmation message.
     */
    fun dismissFeedbackBanner() {
        uiState = uiState.copy(feedbackSuccessMessage = null)
    }

    /**
     * Displays the sign out confirmation alert.
     */
    fun promptSignOut() {
        uiState = uiState.copy(showSignOutDialog = true)
    }

    /**
     * Dismisses the sign out confirmation alert.
     */
    fun dismissSignOut() {
        uiState = uiState.copy(showSignOutDialog = false)
    }

    /**
     * Clears error state and reloads profile.
     */
    fun retry() {
        loadProfile()
    }
}
