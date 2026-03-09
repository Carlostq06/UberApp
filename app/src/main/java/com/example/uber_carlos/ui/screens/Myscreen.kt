package com.example.uber_carlos.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.uber_carlos.ui.components.FilledButtonExample
import com.example.uber_carlos.ui.components.Move
import com.example.uber_carlos.ui.components.Section
import com.example.uber_carlos.ui.components.Tittle

@Composable
fun MyScreen(onNavigateToLogin: () -> Unit) {
    Scaffold(
        containerColor = Color(0xFF276EF1),
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Tittle()
                Section()
                Move()
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 21.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                FilledButtonExample(text = "Get Started", onClick = { onNavigateToLogin() })
            }
        }
    }
}