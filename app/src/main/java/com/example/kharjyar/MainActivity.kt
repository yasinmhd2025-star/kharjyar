package com.example.kharjyar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kharjyar.ui.KharjyarApp
import com.example.kharjyar.ui.KharjyarTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val vm: ExpenseViewModel = viewModel()
            KharjyarTheme(darkTheme = vm.darkMode) {
                KharjyarApp(vm)
            }
        }
    }
}
