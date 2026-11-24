package com.example.kharjyar.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.kharjyar.ExpenseViewModel

@Composable
fun SettingsScreen(vm: ExpenseViewModel) {
    var showClearDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("حالت تاریک", fontWeight = FontWeight.Medium)
                        Text(
                            "تم تیره برای استفاده شبانه",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                    Switch(
                        checked = vm.darkMode,
                        onCheckedChange = { vm.changeDarkMode(it) }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text("واحد پول", fontWeight = FontWeight.Medium)
                CurrencyOption("تومان", vm.currency == "تومان") { vm.changeCurrency("تومان") }
                CurrencyOption("ریال", vm.currency == "ریال") { vm.changeCurrency("ریال") }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = { showClearDialog = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("پاک کردن همه تراکنش‌ها")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("درباره برنامه", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(6.dp))
                Text("خرج‌یار - نسخه 1.0")
                Text(
                    "برنامه‌ای ساده برای ثبت درآمد و هزینه‌های روزانه. پروژه درس برنامه‌نویسی موبایل.",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }

    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            title = { Text("پاک کردن همه تراکنش‌ها") },
            text = { Text("این کار برگشت‌پذیر نیست. مطمئنی؟") },
            confirmButton = {
                TextButton(onClick = {
                    vm.clearAll()
                    showClearDialog = false
                }) { Text("بله، پاک کن") }
            },
            dismissButton = {
                TextButton(onClick = { showClearDialog = false }) { Text("انصراف") }
            }
        )
    }
}

@Composable
private fun CurrencyOption(label: String, selected: Boolean, onSelect: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        RadioButton(selected = selected, onClick = onSelect)
        Text(label)
    }
}
