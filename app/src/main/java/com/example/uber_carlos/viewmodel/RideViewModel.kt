package com.example.uber_carlos.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.uber_carlos.model.Ride
import com.example.uber_carlos.service.UberFcmService
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

sealed interface RideState {
    data object Idle             : RideState
    data object VehicleSelection : RideState
    data object Searching        : RideState
    data class DriverAssigned(
        val driverName: String,
        val etaMinutes: Int
    ) : RideState
    data object InProgress       : RideState
    data object Completed        : RideState
}

class RideViewModel(application: Application) : AndroidViewModel(application) {

    private val auth = FirebaseAuth.getInstance()
    private val db   = FirebaseFirestore.getInstance()
    private val context = application.applicationContext

    var rideState: RideState by mutableStateOf(RideState.Idle)
        private set

    var userLocation: LatLng? by mutableStateOf(null)
        private set

    var destination: LatLng? by mutableStateOf(null)
        private set

    var destinationName: String by mutableStateOf("")
        private set

    var estimatedPrice: Double by mutableStateOf(0.0)
        private set

    // Tiempos diferenciados
    var estimatedUberXTime: String by mutableStateOf("")
        private set
    var estimatedUberBlackTime: String by mutableStateOf("")
        private set

    var driverName: String by mutableStateOf("")
        private set

    var scheduledDate: String by mutableStateOf("")
        private set
    var scheduledTime: String by mutableStateOf("")
        private set

    var rideHistory: List<Ride> by mutableStateOf(emptyList())
        private set

    fun updateUserLocation(latLng: LatLng) {
        userLocation = latLng
    }

    fun setDestination(latLng: LatLng, name: String) {
        destination = latLng
        destinationName = name
        val origin = userLocation ?: return
        
        val distKm = haversineDistance(origin, latLng)
        estimatedPrice = ((2.5 + distKm * 1.2) * 100).toInt() / 100.0
        
        // CÁLCULO DE TIEMPOS (Uber Black es más rápido, UberX demora más)
        val baseMinutes = calculateMinutes(origin, latLng)
        estimatedUberBlackTime = "$baseMinutes min"
        estimatedUberXTime = "${baseMinutes + 3} min"
        
        rideState = RideState.VehicleSelection
    }

    private fun calculateMinutes(origin: LatLng, destination: LatLng): Int {
        val distanceKm = haversineDistance(origin, destination)
        val averageSpeed = 25.0 
        val hours = (distanceKm * 1.2) / averageSpeed
        val minutes = (hours * 60).toInt()
        return if (minutes < 2) 2 else minutes
    }

    fun setSchedule(date: String, time: String) {
        scheduledDate = date
        scheduledTime = time
    }

    fun cancelVehicleSelection() {
        rideState = RideState.Idle
    }

    fun requestRide() {
        if (destination == null || userLocation == null) return
        rideState = RideState.Searching

        viewModelScope.launch {
            delay(2000)
            driverName = listOf("Carlos M.", "Ana R.", "Pedro L.").random()
            val eta = (3..10).random()

            rideState = RideState.DriverAssigned(driverName, eta)
            UberFcmService.showNotification(context, "Conductor encontrado", "$driverName llega en $eta min")

            delay(2000)
            rideState = RideState.InProgress
            delay(3000)

            saveRideToFirestore()
            rideState = RideState.Completed
            UberFcmService.showNotification(context, "Viaje finalizado", "Has llegado a $destinationName")
        }
    }

    private fun saveRideToFirestore() {
        val user = auth.currentUser ?: return
        val finalDate = if (scheduledDate.isNotEmpty()) "$scheduledDate $scheduledTime" else LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))

        val ride = Ride(
            userId = user.uid,
            destName = destinationName,
            driverName = driverName,
            price = estimatedPrice,
            date = finalDate,
            status = "completed"
        )

        viewModelScope.launch {
            try {
                db.collection("rides").add(ride).await()
                loadRideHistory()
            } catch (_: Exception) {}
        }
    }

    fun loadRideHistory() {
        val user = auth.currentUser ?: return
        viewModelScope.launch {
            try {
                val snapshot = db.collection("rides").whereEqualTo("userId", user.uid).get().await()
                rideHistory = snapshot.documents.mapNotNull { it.toObject(Ride::class.java) }
            } catch (_: Exception) {}
        }
    }

    fun resetRide() {
        rideState = RideState.Idle
        scheduledDate = ""
        scheduledTime = ""
        destination = null
    }

    private fun haversineDistance(a: LatLng, b: LatLng): Double {
        val radius = 6371.0
        val dLat = Math.toRadians(b.latitude - a.latitude)
        val dLng = Math.toRadians(b.longitude - a.longitude)
        val h = sin(dLat / 2).pow(2) + cos(Math.toRadians(a.latitude)) * cos(Math.toRadians(b.latitude)) * sin(dLng / 2).pow(2)
        return 2 * radius * asin(sqrt(h))
    }
}
