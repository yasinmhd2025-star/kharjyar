package com.example.kharjyar.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.kharjyar.ExpenseViewModel
import kotlinx.coroutines.launch

private data class DrawerItem(val route: String, val title: String, val icon: ImageVector)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KharjyarApp(vm: ExpenseViewModel) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val items = listOf(
        DrawerItem("home", "خانه", Icons.Filled.Home),
        DrawerItem("transactions", "تراکنش‌ها", Icons.AutoMirrored.Filled.List),
        DrawerItem("settings", "تنظیمات", Icons.Filled.Settings)
    )

    val screenTitle = when (currentRoute) {
        "transactions" -> "تراکنش‌ها"
        "settings" -> "تنظیمات"
        "add" -> "تراکنش جدید"
        else -> "خرج‌یار"
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = currentRoute != "add",
        drawerContent = {
            ModalDrawerSheet {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF0F766E))
                        .padding(24.dp)
                ) {
                    Column {
                        Text("خرج‌یار", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        Text("مدیریت درآمد و هزینه", color = Color.White.copy(alpha = 0.8f))
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                items.forEach { item ->
                    NavigationDrawerItem(
                        label = { Text(item.title) },
                        icon = { Icon(item.icon, contentDescription = null) },
                        selected = currentRoute == item.route,
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate(item.route) {
                                popUpTo("home")
                                launchSingleTop = true
                            }
                        },
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text(screenTitle) },
                    navigationIcon = {
                        if (currentRoute == "add") {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "بازگشت")
                            }
                        } else {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(Icons.Filled.Menu, contentDescription = "منو")
                            }
                        }
                    }
                )
            },
            floatingActionButton = {
                if (currentRoute == "home" || currentRoute == "transactions") {
                    FloatingActionButton(onClick = { navController.navigate("add") }) {
                        Icon(Icons.Filled.Add, contentDescription = "تراکنش جدید")
                    }
                }
            }
        ) { padding ->
            NavHost(
                navController = navController,
                startDestination = "home",
                modifier = Modifier.padding(padding)
            ) {
                composable("home") {
                    DashboardScreen(vm, onSeeAll = {
                        navController.navigate("transactions") {
                            popUpTo("home")
                            launchSingleTop = true
                        }
                    })
                }
                composable("transactions") { TransactionsScreen(vm) }
                composable("settings") { SettingsScreen(vm) }
                composable("add") {
                    AddTransactionScreen(vm, onDone = { navController.popBackStack() })
                }
            }
        }
    }
}
