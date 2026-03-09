package com.example.uber_carlos.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// Imports de Stripe
import com.example.uber_carlos.viewmodel.PaymentState
import com.example.uber_carlos.viewmodel.PaymentViewModel
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import com.stripe.android.paymentsheet.rememberPaymentSheet


fun getMetadataKey(context: android.content.Context, keyName: String): String {
    return try {
        val appInfo = context.packageManager.getApplicationInfo(
            context.packageName,
            android.content.pm.PackageManager.GET_META_DATA
        )
        appInfo.metaData.getString(keyName) ?: ""
    } catch (e: Exception) {
        ""
    }
}

@Composable
fun PaymentScreen(
    paymentVm: PaymentViewModel,
    ridePrice: Double,         // Precio para Stripe
    rideSummary: String,       // Resumen para el backend
    onPaymentOk: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val state = paymentVm.paymentState
    
    BackHandler(enabled = true) {
        onBack()
    }

    LaunchedEffect(Unit) {
        val stripeKey = getMetadataKey(context, "STRIPE_PUBLISHABLE_KEY")
    }

    val paymentSheet = rememberPaymentSheet { result ->
        when (result) {
            is PaymentSheetResult.Completed -> paymentVm.onPaymentSuccess()
            is PaymentSheetResult.Canceled -> paymentVm.onPaymentCancelled()
            is PaymentSheetResult.Failed -> paymentVm.onPaymentFailed(result.error.message)
        }
    }

    LaunchedEffect(state) {
        if (state is PaymentState.ReadyToPay) {
            paymentSheet.presentWithPaymentIntent(
                paymentIntentClientSecret = state.clientSecret,
                configuration = PaymentSheet.Configuration(merchantDisplayName = "Uber Carlos")
            )
        }
        if (state is PaymentState.Success) {
            onPaymentOk()
        }
    }

    // ── TU DISEÑO (SIN TOCAR) ──
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
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Llegada",
                modifier = Modifier.size(80.dp),
                tint = Color(0xFF4CAF50)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Has llegado a tu destino",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Total a pagar",
                fontSize = 16.sp,
                color = Color.Gray
            )

            Text(
                text = "${String.format("%.2f", ridePrice)} €",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Botón con tu estilo pero con la llamada a Stripe
            Button(
                onClick = {
                    // Si ya está cargando, no hacemos nada
                    if (state !is PaymentState.Loading) {
                        paymentVm.createPaymentIntent(ridePrice, rideSummary)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                if (state is PaymentState.Loading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Pagar viaje", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }

            // Texto de error si algo falla en la red
            if (state is PaymentState.Error) {
                Text(
                    text = state.msg,
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}
