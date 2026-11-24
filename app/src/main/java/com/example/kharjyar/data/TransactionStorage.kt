package com.example.kharjyar.data

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

class TransactionStorage(context: Context) {

    private val prefs = context.getSharedPreferences("kharjyar_prefs", Context.MODE_PRIVATE)

    fun load(): List<Transaction> {
        val raw = prefs.getString(KEY_LIST, null) ?: return emptyList()
        val result = mutableListOf<Transaction>()
        try {
            val array = JSONArray(raw)
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                result.add(
                    Transaction(
                        id = obj.getLong("id"),
                        title = obj.getString("title"),
                        amount = obj.getLong("amount"),
                        isExpense = obj.getBoolean("isExpense"),
                        category = obj.getString("category"),
                        date = obj.getLong("date")
                    )
                )
            }
        } catch (e: Exception) {
            return emptyList()
        }
        return result
    }

    fun save(list: List<Transaction>) {
        val array = JSONArray()
        for (t in list) {
            val obj = JSONObject()
            obj.put("id", t.id)
            obj.put("title", t.title)
            obj.put("amount", t.amount)
            obj.put("isExpense", t.isExpense)
            obj.put("category", t.category)
            obj.put("date", t.date)
            array.put(obj)
        }
        prefs.edit().putString(KEY_LIST, array.toString()).apply()
    }

    fun isDarkMode(): Boolean = prefs.getBoolean(KEY_DARK, false)

    fun setDarkMode(value: Boolean) {
        prefs.edit().putBoolean(KEY_DARK, value).apply()
    }

    fun getCurrency(): String = prefs.getString(KEY_CURRENCY, "تومان") ?: "تومان"

    fun setCurrency(value: String) {
        prefs.edit().putString(KEY_CURRENCY, value).apply()
    }

    companion object {
        private const val KEY_LIST = "transactions"
        private const val KEY_DARK = "dark_mode"
        private const val KEY_CURRENCY = "currency"
    }
}
