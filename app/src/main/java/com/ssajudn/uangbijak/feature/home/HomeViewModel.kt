package com.ssajudn.uangbijak.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssajudn.uangbijak.data.model.Transaction
import com.ssajudn.uangbijak.data.model.Wallet
import com.ssajudn.uangbijak.data.repository.TransactionRepository
import com.ssajudn.uangbijak.data.repository.WalletRepository
import com.ssajudn.uangbijak.feature.auth.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val authRepository: AuthRepository,
    private val walletRepository: WalletRepository
) : ViewModel() {
    private val _transactions = MutableStateFlow<List<Transaction>>(emptyList())
    val transactions: StateFlow<List<Transaction>> = _transactions

    private val _wallets = MutableStateFlow<List<Wallet>>(emptyList())
    val wallets: StateFlow<List<Wallet>> = _wallets

    private val _totalBalance = MutableStateFlow(0.0)
    val totalBalance: StateFlow<Double> = _totalBalance

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    val userName: String = authRepository.currentUser?.displayName ?: "Pengguna"

    init {
        loadData()
    }

    private fun loadData() {
        val userId = authRepository.currentUser?.uid

        if (userId != null) {
            viewModelScope.launch {
                launch {
                    transactionRepository.getTransactions(userId).collect { list ->
                        _transactions.value = list
                        _isLoading.value = false
                    }
                }

                launch {
                    walletRepository.getWallets(userId).collect { list ->
                        _wallets.value = list
                        _totalBalance.value = list.sumOf { it.balance }
                    }
                }
            }
        }
    }

    fun logout() {
        authRepository.logout()
    }
}