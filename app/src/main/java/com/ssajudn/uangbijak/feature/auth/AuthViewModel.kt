package com.ssajudn.uangbijak.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _isLoginSuccess = MutableStateFlow(false)
    val isLoginSuccess: StateFlow<Boolean> = _isLoginSuccess

    private val _authStatus = MutableStateFlow<AuthStatus>(AuthStatus.Idle)
    val authStatus: StateFlow<AuthStatus> = _authStatus

    sealed class AuthStatus {
        object Idle : AuthStatus()
        object Success : AuthStatus()
    }

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _errorMessage.value = "Email dan password wajib diisi"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            val result = repository.login(email, password)

            _isLoading.value = false
            result.onSuccess {
                _isLoginSuccess.value = true
            }.onFailure { error ->
                _errorMessage.value = error.message ?: "Login gagal"
            }
        }
    }

    fun register(name: String, email: String, pass: String) {
        if (name.isBlank() || email.isBlank() || pass.isBlank()) {
            _errorMessage.value = "Semua data wajib diisi"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            val result = repository.register(name, email, pass)

            _isLoading.value = false
            result.onSuccess {
                _authStatus.value = AuthStatus.Success
            }.onFailure {
                _errorMessage.value = it.message ?: "Register gagal"
            }
        }
    }

    fun resetStatus() {
        _authStatus.value = AuthStatus.Idle
        _errorMessage.value = null
    }
}