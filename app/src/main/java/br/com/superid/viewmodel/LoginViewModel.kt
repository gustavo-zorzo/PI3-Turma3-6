package br.com.superid.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// Data class para o estado da UI do formulário de login
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val passwordVisible: Boolean = false
    // Adicionar aqui campos para erros de validação, estado de loading, etc. no futuro
)

class LoginViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEmailChange(newEmail: String): Unit {
        _uiState.update { it.copy(email = newEmail) }
    }

    fun onPasswordChange(newPassword: String): Unit {
        _uiState.update { it.copy(password = newPassword) }
    }

    fun togglePasswordVisibility(): Unit {
        _uiState.update { it.copy(passwordVisible = !it.passwordVisible) }
    }

    fun attemptLogin(): Unit {
        // Lógica de validação e login virá aqui
        val currentState = _uiState.value
        println("Tentativa de Login com: ${currentState.email}")
        // Exemplo: chamar API de login, etc.
    }
} 