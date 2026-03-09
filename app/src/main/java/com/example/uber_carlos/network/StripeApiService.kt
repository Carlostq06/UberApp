package com.example.uber_carlos.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

// ═══════════════════════════════════════════
//  Modelos de request/response
// ═══════════════════════════════════════════

// Lo que enviamos al servidor
data class PaymentRequest(
    val amount:      Int,     // En céntimos (1550 = 15.50€)
    val currency:    String = "eur",
    val rideSummary: String = ""
)

// Respuesta para Opción A (PaymentSheet)
data class PaymentIntentResponse(
    val clientSecret: String
)


// ═══════════════════════════════════════════
//  Interfaz Retrofit
// ═══════════════════════════════════════════

interface StripeApiService {

    // Opción A: PaymentSheet
    @POST("create-payment-intent")
    suspend fun createPaymentIntent(
        @Body request: PaymentRequest
    ): PaymentIntentResponse}

// ═══════════════════════════════════════════
//  Singleton de Retrofit para Stripe
// ═══════════════════════════════════════════

object StripeClient {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://uber-clone-backend-mu-cyan.vercel.app/api/")
        // ↑ CAMBIA esto por la URL de TUS Cloud Functions
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: StripeApiService = retrofit.create(StripeApiService::class.java)
}