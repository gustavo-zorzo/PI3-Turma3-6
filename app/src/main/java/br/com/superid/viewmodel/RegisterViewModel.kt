package br.com.superid.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// Data class para o estado da UI do formulário de registro
data class RegisterUiState(
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val termsAccepted: Boolean = false,
    val passwordVisible: Boolean = false,
    val confirmPasswordVisible: Boolean = false
    // Adicionar aqui campos para erros de validação, estado de loading, etc. no futuro
)

class RegisterViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun onUsernameChange(newUsername: String): Unit {
        _uiState.update { it.copy(username = newUsername) }
    }

    fun onEmailChange(newEmail: String): Unit {
        _uiState.update { it.copy(email = newEmail) }
    }

    fun onPasswordChange(newPassword: String): Unit {
        _uiState.update { it.copy(password = newPassword) }
    }

    fun onConfirmPasswordChange(newConfirmPassword: String): Unit {
        _uiState.update { it.copy(confirmPassword = newConfirmPassword) }
    }

    fun onTermsAcceptedChange(accepted: Boolean): Unit {
        _uiState.update { it.copy(termsAccepted = accepted) }
    }

    fun togglePasswordVisibility(): Unit {
        _uiState.update { it.copy(passwordVisible = !it.passwordVisible) }
    }

    fun toggleConfirmPasswordVisibility(): Unit {
        _uiState.update { it.copy(confirmPasswordVisible = !it.confirmPasswordVisible) }
    }

    fun attemptRegistration(): Unit {
        // Lógica de validação e registro virá aqui
        // Por enquanto, apenas simula a tentativa
        val currentState = _uiState.value
        println("Tentativa de Registro com: ${currentState.username}, ${currentState.email}, Termos: ${currentState.termsAccepted}")
        // Exemplo: verificar se senhas coincidem, se termos foram aceitos, etc.
    }
} 