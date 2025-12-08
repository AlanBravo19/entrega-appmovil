package com.example.appmovil.ui.login

import com.example.appmovil.data.SessionManager
import org.junit.Test
import org.junit.Assert.*

class LoginViewModelTest {

    // Fake SessionManager para pruebas
    private class FakeSessionManager(
        private val email: String,
        private val password: String
    ) : SessionManager(null) {

        override fun getEmail(): String = email
        override fun getPassword(): String = password
    }

    @Test
    fun login_correctCredentials_returnsTrue() {
        val session = FakeSessionManager("test@mail.com", "1234")
        val vm = LoginViewModel(session)

        vm.onEmailChanged("test@mail.com")
        vm.onPasswordChanged("1234")

        val result = vm.login()

        assertTrue(result)
    }

    @Test
    fun login_wrongCredentials_returnsFalse() {
        val session = FakeSessionManager("test@mail.com", "1234")
        val vm = LoginViewModel(session)

        vm.onEmailChanged("otro@mail.com")
        vm.onPasswordChanged("0000")

        val result = vm.login()

        assertFalse(result)
    }
}
