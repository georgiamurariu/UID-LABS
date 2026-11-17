package com.example.laboratory_uid.authScreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class AuthViewModel : ViewModel() {
    var username by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set
    var usernameError by mutableStateOf("")
        private set
    var passwordError by mutableStateOf("")
        private set
    var authenticationMessage by mutableStateOf("")
        private set
    var isAuthenticated by mutableStateOf(false)
        private set

    fun updateUsername(value: String) {
        username = value
        usernameError = ""
        clearAuthentication()
    }

    fun updatePassword(value: String) {
        password = value
        passwordError = ""
        clearAuthentication()
    }

    private fun clearAuthentication() {
        isAuthenticated = false
        authenticationMessage = ""
    }

    fun signIn() {
        clearAuthentication()
        usernameError = when {
            username.isBlank() -> "Username cannot be empty!"
            username.length < 3 -> "Username must contain at least 3 characters!"
            else -> ""
        }
        passwordError = when {
            password.isBlank() -> "Password cannot be empty!"
            password.length < 6 -> "Password must contain at least 6 characters!"
            else -> ""
        }
        if (usernameError.isNotEmpty() || passwordError.isNotEmpty()) return

        isAuthenticated = username == "admin" && password == "password"
        authenticationMessage = if (isAuthenticated) {
            "Sign in successful!"
        } else {
            "Invalid username or password!"
        }
    }
}
