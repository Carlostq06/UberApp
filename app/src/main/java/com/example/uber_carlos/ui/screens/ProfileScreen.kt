package com.example.uber_carlos.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.uber_carlos.viewmodel.ProfileViewModel
import com.example.uber_carlos.viewmodel.RideViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel,
    rideViewModel: RideViewModel,
    onBack: () -> Unit,
    onNavigateToHistory: () -> Unit
) {
    val photoUrl = profileViewModel.photoUrl
    val isUploading = profileViewModel.isUploading

    // Refrescar datos al entrar
    LaunchedEffect(Unit) {
        profileViewModel.loadProfile()
        rideViewModel.loadRideHistory()
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { profileViewModel.uploadPhoto(it) }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Perfil", fontWeight = FontWeight.Bold, color = Color.Black) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás", tint = Color.Black)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Foto de Perfil
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFEEEEEE))
                    .clickable { launcher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                if (photoUrl != null) {
                    AsyncImage(
                        model = photoUrl,
                        contentDescription = "Profile Photo",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(60.dp), tint = Color.Gray)
                }
                
                if (isUploading) {
                    CircularProgressIndicator(color = Color.Black)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text("Toca para cambiar foto", fontSize = 12.sp, color = Color.Gray)

            Spacer(modifier = Modifier.height(32.dp))

            // Información
            InfoRow(label = "Email", value = profileViewModel.email)
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
            InfoRow(label = "Teléfono", value = profileViewModel.phone)
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
            
            // Usamos rideViewModel para el contador de viajes
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToHistory() }
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Viajes totales", fontWeight = FontWeight.Medium, color = Color.Gray)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(rideViewModel.rideHistory.size.toString(), fontWeight = FontWeight.Bold, color = Color.Black)
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = Color.Black)
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Cerrar", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontWeight = FontWeight.Medium, color = Color.Gray)
        Text(value, fontWeight = FontWeight.Bold, color = Color.Black)
    }
}
