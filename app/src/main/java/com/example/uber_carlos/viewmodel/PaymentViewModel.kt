package com.example.uber_carlos.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uber_carlos.network.PaymentRequest
import com.example.uber_carlos.network.StripeClient
import kotlinx.coroutines.launch

// Interfaz sellada para manejar el estado del pago de Stripe
sealed interface PaymentState {
    data object Idle    : PaymentState
    data object Loading : PaymentState
    data class ReadyToPay(val clientSecret: String) : PaymentState
    data class CheckoutUrl(val url: String) : PaymentState
    data object Success : PaymentState
    data class  Error(val msg: String) : PaymentState
}

class PaymentViewModel : ViewModel() {

    // Estado observable por la UI
    var paymentState: PaymentState by mutableStateOf(PaymentState.Idle)
        private set

    /**
     * Llama a tu backend (StripeClient) para crear el Payment Intent
     * @param amountEuros El monto en euros (se convierte a céntimos para Stripe)
     * @param rideSummary Descripción del viaje
     */
    fun createPaymentIntent(amountEuros: Double, rideSummary: String) {
        paymentState = PaymentState.Loading

        // Stripe requiere el monto en la unidad mínima (céntimos)
        val amountCents = (amountEuros * 100).toInt()

        viewModelScope.launch {
            try {
                val response = StripeClient.api.createPaymentIntent(
                    PaymentRequest(
                        amount = amountCents,
                        rideSummary = rideSummary
                    )
                )
                // Si la API responde bien, pasamos al estado "Listo para pagar"
                paymentState = PaymentState.ReadyToPay(response.clientSecret)
            } catch (e: Exception) {
                paymentState = PaymentState.Error(e.localizedMessage ?: "Error al crear pago")
            }
        }
    }

    // Funciones para actualizar el estado según el resultado de Stripe SDK
    fun onPaymentSuccess() {
        paymentState = PaymentState.Success
    }

    fun onPaymentFailed(msg: String?) {
        paymentState = PaymentState.Error(msg ?: "Pago fallido")
    }

    fun onPaymentCancelled() {
        paymentState = PaymentState.Idle
    }

    fun resetPayment() {
        paymentState = PaymentState.Idle
    }
}