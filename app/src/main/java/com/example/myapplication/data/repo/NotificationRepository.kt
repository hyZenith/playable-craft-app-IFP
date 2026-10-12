package com.example.myapplication.data.repo

import com.example.myapplication.data.model.NotificationItem
import kotlinx.coroutines.delay

/**
 * Repository interface managing user notification alerts, unread states, and clearing actions.
 */
interface NotificationRepository {
    /**
     * Retrieves all notifications for the active user.
     */
    suspend fun getNotifications(): Result<List<NotificationItem>>

    /**
     * Marks a single notification as read.
     */
    suspend fun markAsRead(id: String): Result<Unit>

    /**
     * Marks all active notifications as read.
     */
    suspend fun markAllAsRead(): Result<Unit>

    /**
     * Dismisses and removes a specific notification item.
     */
    suspend fun clearNotification(id: String): Result<Unit>
}

/**
 * In-memory repository implementation simulating empty state matching the reference design.
 */
class FakeNotificationRepository(
    initialNotifications: List<NotificationItem> = emptyList()
) : NotificationRepository {

    private val notifications = initialNotifications.toMutableList()

    override suspend fun getNotifications(): Result<List<NotificationItem>> {
        delay(200)
        return Result.success(notifications.toList())
    }

    override suspend fun markAsRead(id: String): Result<Unit> {
        val index = notifications.indexOfFirst { it.id == id }
        if (index != -1) {
            notifications[index] = notifications[index].copy(isRead = true)
        }
        return Result.success(Unit)
    }

    override suspend fun markAllAsRead(): Result<Unit> {
        val updated = notifications.map { it.copy(isRead = true) }
        notifications.clear()
        notifications.addAll(updated)
        return Result.success(Unit)
    }

    override suspend fun clearNotification(id: String): Result<Unit> {
        notifications.removeAll { it.id == id }
        return Result.success(Unit)
    }
}
