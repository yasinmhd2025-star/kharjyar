package com.example.kharjyar.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kharjyar.ExpenseViewModel
import com.example.kharjyar.formatMoney

@Composable
fun DashboardScreen(vm: ExpenseViewModel, onSeeAll: () -> Unit) {
    val unit = vm.currency

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            BalanceCard(balance = formatMoney(vm.balance, unit))
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                SummaryBox("درآمد", formatMoney(vm.totalIncome, unit), IncomeColor, Modifier.weight(1f))
                SummaryBox("هزینه", formatMoney(vm.totalExpense, unit), ExpenseColor, Modifier.weight(1f))
            }
        }
        item {
            ChartCard(data = vm.expenseByCategory())
        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("آخرین تراکنش‌ها", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                TextButton(onClick = onSeeAll) { Text("مشاهده همه") }
            }
        }
        if (vm.transactions.isEmpty()) {
            item {
                Text(
                    "هنوز تراکنشی ثبت نشده. با دکمه + شروع کن!",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
        items(vm.transactions.take(5), key = { it.id }) { t ->
            TransactionRow(t, unit)
        }
        item { Spacer(modifier = Modifier.height(70.dp)) }
    }
}

@Composable
private fun BalanceCard(balance: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.horizontalGradient(listOf(Color(0xFF0F766E), Color(0xFF14B8A6))))
                .padding(24.dp)
        ) {
            Column {
                Text("موجودی کل", color = Color.White.copy(alpha = 0.85f))
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = balance,
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun SummaryBox(title: String, value: String, color: Color, modifier: Modifier = Modifier) {
    Card(modifier = modifier, shape = RoundedCornerShape(18.dp)) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(title, color = color, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(value, fontSize = 14.sp)
        }
    }
}

@Composable
private fun ChartCard(data: List<Pair<String, Long>>) {
    val total = data.sumOf { it.second }

    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(18.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("هزینه‌ها بر اساس دسته", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))

            if (data.isEmpty()) {
                Text(
                    "هنوز هزینه‌ای ثبت نشده",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            } else {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // نمودار دایره‌ای ساده با drawArc
                    Canvas(modifier = Modifier.size(130.dp)) {
                        val strokeWidth = 34f
                        var startAngle = -90f
                        data.forEachIndexed { index, item ->
                            val sweep = item.second.toFloat() / total.toFloat() * 360f
                            drawArc(
                                color = chartColors[index % chartColors.size],
                                startAngle = startAngle,
                                sweepAngle = sweep,
                                useCenter = false,
                                topLeft = Offset(strokeWidth / 2, strokeWidth / 2),
                                size = Size(size.width - strokeWidth, size.height - strokeWidth),
                                style = Stroke(width = strokeWidth)
                            )
                            startAngle += sweep
                        }
                    }
                    Spacer(modifier = Modifier.width(20.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        data.forEachIndexed { index, item ->
                            val percent = (item.second * 100 / total).toInt()
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(chartColors[index % chartColors.size])
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("${item.first}  $percent%", fontSize = 13.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}
