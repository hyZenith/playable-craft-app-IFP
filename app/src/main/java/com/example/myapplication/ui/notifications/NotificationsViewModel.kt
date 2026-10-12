package com.example.myapplication.ui.notifications

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.NotificationItem
import com.example.myapplication.data.repo.NotificationRepository
import kotlinx.coroutines.launch

/**
 * Immutable UI state for the Notifications screen.
 *
 * @property notifications Current list of notifications.
 * @property isLoading Indicates whether notifications are being fetched.
 * @property errorMessage Non-null if an error occurred during loading.
 */
data class NotificationsUiState(
    val notifications: List<NotificationItem> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

/**
 * ViewModel orchestrating notifications retrieval, read status, and dismissal.
 */
class NotificationsViewModel(
    private val repository: NotificationRepository
) : ViewModel() {

    var uiState by mutableStateOf(NotificationsUiState())
        private set

    init {
        loadNotifications()
    }

    /**
     * Loads notifications from the repository.
     */
    fun loadNotifications() {
        uiState = uiState.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            val result = repository.getNotifications()
            if (result.isSuccess) {
                uiState = uiState.copy(
                    isLoading = false,
                    notifications = result.getOrDefault(emptyList())
                )
            } else {
                uiState = uiState.copy(
                    isLoading = false,
                    errorMessage = "Unable to load notifications. Please try again."
                )
            }
        }
    }

    /**
     * Marks a notification as read.
     */
    fun markAsRead(id: String) {
        viewModelScope.launch {
            repository.markAsRead(id)
            uiState = uiState.copy(
                notifications = uiState.notifications.map {
                    if (it.id == id) it.copy(isRead = true) else it
                }
            )
        }
    }

    /**
     * Marks all notifications as read.
     */
    fun markAllAsRead() {
        viewModelScope.launch {
            repository.markAllAsRead()
            uiState = uiState.copy(
                notifications = uiState.notifications.map { it.copy(isRead = true) }
            )
        }
    }

    /**
     * Dismisses a single notification.
     */
    fun clearNotification(id: String) {
        viewModelScope.launch {
            repository.clearNotification(id)
            uiState = uiState.copy(
                notifications = uiState.notifications.filterNot { it.id == id }
            )
        }
    }

    /**
     * Retries loading notifications.
     */
    fun retry() {
        loadNotifications()
    }
}
