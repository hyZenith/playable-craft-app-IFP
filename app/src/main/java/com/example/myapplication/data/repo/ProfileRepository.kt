package com.example.myapplication.data.repo

import com.example.myapplication.data.model.UserProfile
import kotlinx.coroutines.delay

/**
 * Repository interface for managing user profile details, credentials, and feedback submissions.
 */
interface ProfileRepository {
    /**
     * Retrieves the current user's profile details.
     */
    suspend fun getProfile(): Result<UserProfile>

    /**
     * Updates editable personal profile information.
     */
    suspend fun updateProfile(fullName: String, email: String): Result<UserProfile>

    /**
     * Submits user feedback or feature requests.
     */
    suspend fun submitFeedback(feedback: String): Result<Unit>
}

/**
 * In-memory repository implementation simulating profile operations matching the reference design.
 */
class FakeProfileRepository(
    initialProfile: UserProfile = UserProfile(
        fullName = "Anurag Bezboruah",
        email = "businessandanurag@gmail.com",
        role = "Teacher",
        memberSince = "Sep 2024"
    )
) : ProfileRepository {

    private var profile: UserProfile = initialProfile

    override suspend fun getProfile(): Result<UserProfile> {
        delay(200)
        return Result.success(profile)
    }

    override suspend fun updateProfile(fullName: String, email: String): Result<UserProfile> {
        delay(300)
        profile = profile.copy(
            fullName = fullName.trim(),
            email = email.trim()
        )
        return Result.success(profile)
    }

    override suspend fun submitFeedback(feedback: String): Result<Unit> {
        delay(400)
        return Result.success(Unit)
    }
}
