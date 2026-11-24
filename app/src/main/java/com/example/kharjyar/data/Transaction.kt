package com.example.kharjyar.data

// یک تراکنش مالی (درآمد یا هزینه)
data class Transaction(
    val id: Long,
    val title: String,
    val amount: Long,
    val isExpense: Boolean,
    val category: String,
    val date: Long
)

object Categories {
    val expense = listOf("خوراک", "حمل‌ونقل", "خرید", "قبض", "سلامت", "سرگرمی", "سایر")
    val income = listOf("حقوق", "هدیه", "فروش", "سایر")
}
