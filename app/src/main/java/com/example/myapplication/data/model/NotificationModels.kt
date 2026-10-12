package com.example.myapplication.data.model

/**
 * Functional classification of notification events.
 */
enum class NotificationType {
    SYSTEM,
    SIMULATION,
    LESSON_PLAN,
    MCQ,
    DELIVERY
}

/**
 * Notification event alerting teachers about generation results, delivery reminders, or system updates.
 *
 * @property id Unique notification identifier.
 * @property title Concise notification summary.
 * @property description Detailed explanation or status update.
 * @property timestamp Relative or formatted time string (e.g., "10m ago", "Yesterday").
 * @property isRead Whether the notification has been seen by the user.
 * @property type Category of the notification event.
 */
data class NotificationItem(
    val id: String,
    val title: String,
    val description: String,
    val timestamp: String,
    val isRead: Boolean = false,
    val type: NotificationType = NotificationType.SYSTEM
)
