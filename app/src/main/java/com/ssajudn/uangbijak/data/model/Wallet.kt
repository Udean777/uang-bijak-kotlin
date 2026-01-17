package com.ssajudn.uangbijak.data.model

data class Wallet(
    var id: String = "",
    val userId: String = "",
    val name: String = "",
    val type: String = "cash",
    val balance: Double = 0.0,
    val color: String = "#3B82F6"
)