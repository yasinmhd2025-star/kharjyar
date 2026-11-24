package com.example.kharjyar

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.example.kharjyar.data.Transaction
import com.example.kharjyar.data.TransactionStorage

class ExpenseViewModel(app: Application) : AndroidViewModel(app) {

    private val storage = TransactionStorage(app)

    var transactions by mutableStateOf(storage.load())
        private set

    var darkMode by mutableStateOf(storage.isDarkMode())
        private set

    var currency by mutableStateOf(storage.getCurrency())
        private set

    val totalIncome: Long
        get() = transactions.filter { !it.isExpense }.sumOf { it.amount }

    val totalExpense: Long
        get() = transactions.filter { it.isExpense }.sumOf { it.amount }

    val balance: Long
        get() = totalIncome - totalExpense

    // جمع هزینه‌ها به تفکیک دسته، از بیشترین به کمترین
    fun expenseByCategory(): List<Pair<String, Long>> {
        return transactions
            .filter { it.isExpense }
            .groupBy { it.category }
            .map { entry -> entry.key to entry.value.sumOf { it.amount } }
            .sortedByDescending { it.second }
    }

    fun addTransaction(t: Transaction) {
        // جدیدترین همیشه اول لیسته
        transactions = listOf(t) + transactions
        storage.save(transactions)
    }

    fun deleteTransaction(id: Long) {
        transactions = transactions.filter { it.id != id }
        storage.save(transactions)
    }

    fun clearAll() {
        transactions = emptyList()
        storage.save(transactions)
    }

    fun changeDarkMode(value: Boolean) {
        darkMode = value
        storage.setDarkMode(value)
    }

    fun changeCurrency(value: String) {
        currency = value
        storage.setCurrency(value)
    }
}
