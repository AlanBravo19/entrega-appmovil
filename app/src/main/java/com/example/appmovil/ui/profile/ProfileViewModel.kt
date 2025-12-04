package com.example.appmovil.ui.profile

import androidx.lifecycle.ViewModel
import com.example.appmovil.data.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class ProfileUiState(
    val name: String = "",
    val email: String = "",
    val address: String = "",
    val phone: String = "",
    val message: String? = null
)

class ProfileViewModel(private val session: SessionManager) : ViewModel() {

    private val _uiState = MutableStateFlow(
        ProfileUiState(
            name = session.getName() ?: "",
            email = session.getEmail() ?: "",  // SOLO lectura
            address = session.getAddress() ?: "",
            phone = session.getPhone() ?: ""
        )
    )
    val uiState: StateFlow<ProfileUiState> = _uiState

    fun onNameChanged(value: String) {
        _uiState.value = _uiState.value.copy(name = value)
    }

    fun onAddressChanged(value: String) {
        _uiState.value = _uiState.value.copy(address = value)
    }

    fun onPhoneChanged(value: String) {
        _uiState.value = _uiState.value.copy(phone = value)
    }

    fun saveChanges(): Boolean {
        val state = _uiState.value

        if (state.name.isBlank()) {
            _uiState.value = state.copy(message = "El nombre es obligatorio")
            return false
        }

        // NO actualizar email → solo datos editables
        session.updateUser(
            name = state.name,
            address = state.address,
            phone = state.phone
        )

        _uiState.value = state.copy(message = "Datos actualizados correctamente")
        return true
    }
}
