package com.example.kharjyar.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.kharjyar.ExpenseViewModel
import com.example.kharjyar.data.Categories
import com.example.kharjyar.data.Transaction

@Composable
fun AddTransactionScreen(vm: ExpenseViewModel, onDone: () -> Unit) {
    val context = LocalContext.current

    var isExpense by remember { mutableStateOf(true) }
    var title by remember { mutableStateOf("") }
    var amountText by remember { mutableStateOf("") }
    var category by remember { mutableStateOf(Categories.expense.first()) }

    val categories = if (isExpense) Categories.expense else Categories.income

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text("نوع تراکنش", fontWeight = FontWeight.Bold)
        Row(
            modifier = Modifier.padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = isExpense,
                onClick = {
                    isExpense = true
                    category = Categories.expense.first()
                },
                label = { Text("هزینه") }
            )
            FilterChip(
                selected = !isExpense,
                onClick = {
                    isExpense = false
                    category = Categories.income.first()
                },
                label = { Text("درآمد") }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("عنوان") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = amountText,
            onValueChange = { newValue ->
                // فقط رقم قبول می‌کنیم
                if (newValue.all { it.isDigit() } && newValue.length <= 12) {
                    amountText = newValue
                }
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("مبلغ (${vm.currency})") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text("دسته‌بندی", fontWeight = FontWeight.Bold)
        LazyRow(
            modifier = Modifier.padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categories) { cat ->
                FilterChip(
                    selected = category == cat,
                    onClick = { category = cat },
                    label = { Text(cat) }
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        Button(
            onClick = {
                val amount = amountText.toLongOrNull()
                if (title.isBlank()) {
                    Toast.makeText(context, "عنوان رو وارد کن", Toast.LENGTH_SHORT).show()
                } else if (amount == null || amount <= 0) {
                    Toast.makeText(context, "مبلغ معتبر وارد کن", Toast.LENGTH_SHORT).show()
                } else {
                    val now = System.currentTimeMillis()
                    vm.addTransaction(
                        Transaction(
                            id = now,
                            title = title.trim(),
                            amount = amount,
                            isExpense = isExpense,
                            category = category,
                            date = now
                        )
                    )
                    onDone()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("ثبت تراکنش")
        }
    }
}
