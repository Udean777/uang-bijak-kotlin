package com.ssajudn.uangbijak.core.util

import java.text.NumberFormat
import java.util.Locale

object CurrencyUtils {
    fun toRupiah(amount: Double): String {
        val format = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        format.maximumFractionDigits = 0
        return format.format(amount)
    }
}