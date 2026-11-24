package com.example.kharjyar

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// عدد رو با ویرگول جدا می‌کنه: 1250000 -> 1,250,000
fun formatMoney(amount: Long, unit: String): String {
    return String.format(Locale.US, "%,d", amount) + " " + unit
}

fun formatDate(time: Long): String {
    return SimpleDateFormat("yyyy/MM/dd", Locale.US).format(Date(time))
}
