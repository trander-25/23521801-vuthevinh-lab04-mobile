package com.example.health_measure_application

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.health_measure_application.ui.HealthApp
import com.example.health_measure_application.ui.theme.Health_Measure_ApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Health_Measure_ApplicationTheme {
                HealthApp()
            }
        }
    }
}
