package com.example.myapplication.data.model

/**
 * User profile information reflecting identity and membership credentials.
 *
 * @property fullName Full legal or display name of the user.
 * @property email Registered account email address.
 * @property role Account role (e.g., "Teacher", "Tutor", "School").
 * @property memberSince Formatted registration date (e.g., "Sep 2024").
 * @property avatarUrl Optional remote avatar image URL.
 */
data class UserProfile(
    val fullName: String,
    val email: String,
    val role: String,
    val memberSince: String,
    val avatarUrl: String? = null
) {
    /**
     * Two-letter initials generated from the full name for the circular avatar placeholder.
     */
    val initials: String
        get() {
            val parts = fullName.trim().split("\\s+".toRegex()).filter { it.isNotEmpty() }
            return when {
                parts.isEmpty() -> "U"
                parts.size == 1 -> parts[0].take(2).uppercase()
                else -> "${parts[0].first()}${parts[1].first()}".uppercase()
            }
        }
}
