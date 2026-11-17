package com.example.laboratory_uid

import com.example.laboratory_uid.authScreen.AuthViewModel
import org.junit.Assert.*
import org.junit.Test

class AuthViewModelTest {
    @Test
    fun emptyFieldsShowBothErrors() {
        val model = AuthViewModel()
        model.signIn()
        assertEquals("Username cannot be empty!", model.usernameError)
        assertEquals("Password cannot be empty!", model.passwordError)
        assertFalse(model.isAuthenticated)
        assertEquals("", model.authenticationMessage)
    }

    @Test
    fun shortFieldsShowBothErrors() {
        val model = AuthViewModel()
        model.updateUsername("ab")
        model.updatePassword("12345")
        model.signIn()
        assertTrue(model.usernameError.isNotEmpty())
        assertTrue(model.passwordError.isNotEmpty())
        assertFalse(model.isAuthenticated)
        assertEquals("", model.authenticationMessage)
    }

    @Test
    fun minimumLengthsPassValidationButFailAuthentication() {
        val model = AuthViewModel()
        model.updateUsername("abc")
        model.updatePassword("123456")
        model.signIn()
        assertEquals("", model.usernameError)
        assertEquals("", model.passwordError)
        assertFalse(model.isAuthenticated)
        assertEquals("Invalid username or password!", model.authenticationMessage)
    }

    @Test
    fun onlyExactCredentialsSucceed() {
        val model = AuthViewModel()
        for ((username, password) in listOf(
            "Admin" to "password",
            "admin" to "Password",
            "other" to "password",
            "admin" to "wrongpass",
            " admin" to "password"
        )) {
            model.updateUsername(username)
            model.updatePassword(password)
            model.signIn()
            assertFalse(model.isAuthenticated)
            assertEquals("Invalid username or password!", model.authenticationMessage)
        }
        model.updateUsername("admin")
        model.updatePassword("password")
        model.signIn()
        assertTrue(model.isAuthenticated)
        assertEquals("Sign in successful!", model.authenticationMessage)

        model.updatePassword("")
        model.signIn()
        assertFalse(model.isAuthenticated)
        assertTrue(model.passwordError.isNotEmpty())
        assertEquals("", model.authenticationMessage)
    }

    @Test
    fun whitespaceFieldsAreRejected() {
        val model = AuthViewModel()
        model.updateUsername("   ")
        model.updatePassword("      ")
        model.signIn()
        assertEquals("Username cannot be empty!", model.usernameError)
        assertEquals("Password cannot be empty!", model.passwordError)
        assertFalse(model.isAuthenticated)
    }
}
