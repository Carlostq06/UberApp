package com.example.uber_carlos.viewmodel

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ProfileViewModel : ViewModel() {

    private val auth    = FirebaseAuth.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val db      = FirebaseFirestore.getInstance()

    var photoUrl   by mutableStateOf<String?>(null)
        private set
    var totalRides by mutableStateOf(0)
        private set
    var isUploading by mutableStateOf(false)
        private set

    val email: String
        get() = auth.currentUser?.email ?: "Sin email"

    val phone: String
        get() = auth.currentUser?.phoneNumber ?: "Sin teléfono"

    init { loadProfile() }

    fun loadProfile() {
        val uid = auth.currentUser?.uid ?: return
        viewModelScope.launch {
            try {
                // Consultar viajes para el usuario actual
                val rides = db.collection("rides")
                    .whereEqualTo("userId", uid)
                    .get().await()
                totalRides = rides.size()
            } catch (e: Exception) {
                Log.e("Profile", "Error al cargar viajes", e)
            }

            try {
                // Cargar foto de perfil
                val ref = storage.reference.child("profile_photos/$uid.jpg")
                photoUrl = ref.downloadUrl.await().toString()
            } catch (_: Exception) {
                // Si no hay foto, photoUrl se mantiene null
            }
        }
    }

    fun uploadPhoto(imageUri: Uri) {
        val uid = auth.currentUser?.uid ?: return
        isUploading = true

        viewModelScope.launch {
            try {
                val ref = storage.reference.child("profile_photos/$uid.jpg")
                ref.putFile(imageUri).await()
                photoUrl = ref.downloadUrl.await().toString()
                
                // Refrescamos el perfil por si acaso
                loadProfile()
            } catch (e: Exception) {
                Log.e("Profile", "Error al subir foto", e)
            } finally {
                isUploading = false
            }
        }
    }
}
