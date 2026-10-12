package com.example.myapplication.session

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.model.LoginResponse

/**
 * Holds the logged-in user in memory.
 * TODO (step 2): persist token + user (DataStore) and restore on app start via GET /api/auth/me.
 */
class SessionViewModel : ViewModel() {

    var user by mutableStateOf<LoginResponse?>(null)
        private set

    var keepSignedIn by mutableStateOf(false)
        private set

    fun onLoggedIn(user: LoginResponse, keepSignedIn: Boolean) {
        this.user = user
        this.keepSignedIn = keepSignedIn
    }

    fun onLoggedOut() {
        user = null
        keepSignedIn = false
    }
}
