package com.stronov.expensetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.stronov.expensetracker.ui.home.HomeScreen
import com.stronov.expensetracker.ui.theme.DuetlyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DuetlyTheme {
                HomeScreen()
            }
        }
    }
}
