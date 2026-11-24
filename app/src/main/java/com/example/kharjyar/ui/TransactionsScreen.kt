package com.example.kharjyar.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.kharjyar.ExpenseViewModel
import com.example.kharjyar.data.Transaction

@Composable
fun TransactionsScreen(vm: ExpenseViewModel) {
    // 0 = همه ، 1 = درآمد ، 2 = هزینه
    var filter by remember { mutableIntStateOf(0) }
    var toDelete by remember { mutableStateOf<Transaction?>(null) }

    val shown = when (filter) {
        1 -> vm.transactions.filter { !it.isExpense }
        2 -> vm.transactions.filter { it.isExpense }
        else -> vm.transactions
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(selected = filter == 0, onClick = { filter = 0 }, label = { Text("همه") })
            FilterChip(selected = filter == 1, onClick = { filter = 1 }, label = { Text("درآمد") })
            FilterChip(selected = filter == 2, onClick = { filter = 2 }, label = { Text("هزینه") })
        }

        if (shown.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "تراکنشی وجود ندارد",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 90.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(shown, key = { it.id }) { t ->
                    TransactionRow(t, vm.currency, onDelete = { toDelete = t })
                }
            }
        }
    }

    toDelete?.let { t ->
        AlertDialog(
            onDismissRequest = { toDelete = null },
            title = { Text("حذف تراکنش") },
            text = { Text("تراکنش «${t.title}» حذف بشه؟") },
            confirmButton = {
                TextButton(onClick = {
                    vm.deleteTransaction(t.id)
                    toDelete = null
                }) { Text("حذف") }
            },
            dismissButton = {
                TextButton(onClick = { toDelete = null }) { Text("انصراف") }
            }
        )
    }
}
