package com.example.appmovil.ui.login

import androidx.lifecycle.ViewModel
import com.example.appmovil.data.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class RegisterUiState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirm: String = "",
    val errorMessage: String? = null
)

class RegisterViewModel(private val session: SessionManager) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState

    fun onNameChanged(value: String) {
        _uiState.value = _uiState.value.copy(name = value)
    }

    fun onEmailChanged(value: String) {
        _uiState.value = _uiState.value.copy(email = value)
    }

    fun onPasswordChanged(value: String) {
        _uiState.value = _uiState.value.copy(password = value)
    }

    fun onConfirmChanged(value: String) {
        _uiState.value = _uiState.value.copy(confirm = value)
    }

    fun register(): Boolean {
        val state = _uiState.value

        if (state.password != state.confirm) {
            _uiState.value = state.copy(errorMessage = "Las contraseñas no coinciden")
            return false
        }

        if (state.email.isBlank() || state.name.isBlank()) {
            _uiState.value = state.copy(errorMessage = "Todos los campos son obligatorios")
            return false
        }

        // Guardar usuario
        session.saveUser(
            name = state.name,
            email = state.email,
            password = state.password
        )

        return true
    }
}
