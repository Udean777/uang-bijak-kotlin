package com.ssajudn.uangbijak.data.model

import com.google.firebase.Timestamp

data class Transaction(
    var id: String = "",
    val userId: String = "",
    val walletId: String = "",
    val amount: Double = 0.0,
    val type: String = "expense",
    val category: String = "",
    val note: String = "",
    val date: Timestamp = Timestamp.now()
)