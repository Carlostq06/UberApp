package com.example.uber_carlos.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uber_carlos.viewmodel.RideViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    viewModel: RideViewModel,
    onBack: () -> Unit
) {
    val history = viewModel.rideHistory

    LaunchedEffect(Unit) { //una vez que se ejecuta
        viewModel.loadRideHistory()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Tus viajes", fontWeight = FontWeight.Bold, color = Color.Black) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás",tint = Color.Black)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color.White
    ) { innerPadding ->
        if (history.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                Text("Aún no tienes viajes registrados", color = Color.Gray)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(innerPadding)
            ) {
                items(history) { ride ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocationOn, contentDescription = null, modifier = Modifier.size(20.dp), tint = Color.Black)
                            Spacer(Modifier.width(8.dp))
                            Text(ride.destName, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black)
                        }
                        
                        Spacer(Modifier.height(4.dp))
                        
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(start = 28.dp)) {
                            Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color.Gray)
                            Spacer(Modifier.width(4.dp))
                            Text("Conductor: ${ride.driverName}", fontSize = 14.sp, color = Color.Gray)
                        }

                        Text("${ride.date} • ${String.format("%.2f", ride.price)} €", fontSize = 14.sp, color = Color.Gray, modifier = Modifier.padding(start = 28.dp))
                        HorizontalDivider(modifier = Modifier.padding(top = 16.dp), color = Color(0xFFEEEEEE))
                    }
                }
            }
        }
    }
}
