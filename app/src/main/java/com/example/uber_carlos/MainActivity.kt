package com.example.uber_carlos

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.uber_carlos.navigation.AppNavigation
import com.example.uber_carlos.ui.theme.UberCarlosTheme
import com.google.android.libraries.places.api.Places
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Log.d("FCM", "Permiso de notificaciones concedido")
        } else {
            Log.w("FCM", "Permiso de notificaciones denegado")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        
        // Solicitar permiso de notificaciones (Android 13+)
        askNotificationPermission()

        // Obtener el Token actual para pruebas
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("FCM", "Token actual: ${task.result}")
            }
        }

        // Initialize Places SDK
        if (!Places.isInitialized()) {
            val apiKey = packageManager
                .getApplicationInfo(packageName, PackageManager.GET_META_DATA)
                .metaData.getString("com.google.android.geo.API_KEY") ?: ""
            Places.initialize(applicationContext, apiKey)
        }
        
        enableEdgeToEdge()
        setContent {
            UberCarlosTheme {
                AppNavigation()
            }
        }
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // Ya tenemos permiso
            } else {
                // Solicitar permiso
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}
