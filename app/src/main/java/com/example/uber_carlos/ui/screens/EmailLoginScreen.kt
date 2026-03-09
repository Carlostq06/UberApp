package com.example.uber_carlos.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uber_carlos.viewmodel.AuthState
import com.example.uber_carlos.viewmodel.AuthViewModel

@Composable
fun EmailLoginScreen(
    viewModel: AuthViewModel,
    onBack: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onSuccess: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val state = viewModel.authState

    LaunchedEffect(state) {
        if (state is AuthState.Authenticated) {
            onSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp)
    ) {
        IconButton(onClick = onBack, modifier = Modifier.offset(x = (-12).dp)) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.Black)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Welcome back",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Input de Email
        Text("Email", fontSize = 14.sp, color = Color.Gray, modifier = Modifier.padding(bottom = 8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(Color(0xFFF1F1F1), RectangleShape)
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            BasicTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                cursorBrush = SolidColor(Color.Black),
                textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Input de Password
        Text("Password", fontSize = 14.sp, color = Color.Gray, modifier = Modifier.padding(bottom = 8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(Color(0xFFF1F1F1), RectangleShape)
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            BasicTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                cursorBrush = SolidColor(Color.Black),
                textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
        }

        if (state is AuthState.Error) {
            Text(state.message, color = Color.Red, fontSize = 12.sp, modifier = Modifier.padding(top = 8.dp))
        }

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = { viewModel.login(email, password) },
            modifier = Modifier.fillMaxWidth().height(54.dp),
            shape = RectangleShape,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
            enabled = state !is AuthState.Loading
        ) {
            if (state is AuthState.Loading) {
                CircularProgressIndicator(color = Color.Black, modifier = Modifier.size(24.dp))
            } else {
                Text("Log in", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }

        TextButton(
            onClick = onNavigateToRegister,
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 16.dp)
        ) {
            Text("Don't have an account? Sign up", color = Color.Black)
        }
    }
}
