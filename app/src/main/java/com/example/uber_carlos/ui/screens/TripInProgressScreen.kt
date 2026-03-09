package com.example.uber_carlos.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uber_carlos.viewmodel.RideState
import com.example.uber_carlos.viewmodel.RideViewModel

@Composable
fun TripInProgressScreen(
    rideViewModel: RideViewModel
) {
    val state = rideViewModel.rideState

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(24.dp)
        ) {
            when (state) {
                is RideState.DriverAssigned -> {
                    Surface(
                        modifier = Modifier.size(100.dp),
                        shape = CircleShape,
                        color = Color(0xFFF3F3F3)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                modifier = Modifier.size(50.dp),
                                tint = Color.Black
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Text(
                        text = "¡Conductor encontrado!",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F8F8)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = state.driverName,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            Text(
                                text = "Llega en ${state.etaMinutes} min",
                                fontSize = 16.sp,
                                color = Color.Gray
                            )
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            HorizontalDivider(color = Color(0xFFE0E0E0))
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            Text(
                                text = "Total estimado",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                            Text(
                                text = "${String.format("%.2f", rideViewModel.estimatedPrice)} €",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.Black
                            )
                        }
                    }
                }

                is RideState.InProgress -> {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(80.dp),
                        tint = Color.Black
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Viaje en curso...",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Información del destino
                    Text(
                        text = "Vas de camino a",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = rideViewModel.destinationName,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )

                    // MOSTRAR FECHA PROGRAMADA (Si existe)
                    if (rideViewModel.scheduledDate.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(24.dp))
                        Surface(
                            color = Color(0xFFE7F0FF),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.DateRange, null, modifier = Modifier.size(16.dp), tint = Color(0xFF276EF1))
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    text = "Programado: ${rideViewModel.scheduledDate} a las ${rideViewModel.scheduledTime}",
                                    fontSize = 14.sp,
                                    color = Color(0xFF276EF1),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(48.dp))

                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth().height(8.dp),
                        color = Color.Black,
                        trackColor = Color(0xFFEEEEEE)
                    )
                }
                
                else -> {
                    CircularProgressIndicator(color = Color.Black)
                }
            }
        }
    }
}
