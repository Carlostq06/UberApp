import java.util.Properties


plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.uber_carlos"
    compileSdk = 36

    defaultConfig {
        val props = Properties()
        val propFile = rootProject.file("local.properties")
        if (propFile.exists()) {
            props.load(propFile.inputStream())
        }

        // 1. Guardamos las claves en variables
        val mapsKey = props.getProperty("MAPS_API_KEY", "")
        val stripeKey = props.getProperty("STRIPE_PUBLISHABLE_KEY", "")

        // 2. Para el AndroidManifest (Google Maps la necesita ahí)
        manifestPlaceholders["MAPS_API_KEY"] = mapsKey

        // 3. Para usar en tu código Kotlin (Como el truco del BuildConfig te daba error,
        // usaremos manifestPlaceholders también para Stripe por si acaso)
        manifestPlaceholders["STRIPE_PUBLISHABLE_KEY"] = stripeKey

        applicationId = "com.example.uber_carlos"
        minSdk = 33
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    // Firebase BoM (import first to manage versions)
    implementation(platform("com.google.firebase:firebase-bom:34.9.0"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.material)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.compose.foundation)
    
    // Use BoM version for Firebase Auth
    implementation("com.google.firebase:firebase-auth")
    
    implementation(libs.androidx.compose.runtime)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    
    implementation("androidx.navigation:navigation-compose:2.9.7")
    implementation("com.googlecode.libphonenumber:libphonenumber:8.13.35")
    implementation("androidx.core:core-splashscreen:1.0.1")
    
    implementation("androidx.credentials:credentials:1.3.0")
    // ↑ API unificada de Android para manejar credenciales
    // (contraseñas, passkeys, tokens de Google, etc.).
    // Reemplaza al antiguo GoogleSignInClient (deprecado).

    implementation("androidx.credentials:credentials-play-services-auth:1.3.0")
    // ↑ Conecta Credential Manager con Google Play Services.
    // Es el "puente" que hace que Credential Manager pueda
    // mostrar el selector de cuentas de Google en el dispositivo.

    implementation("com.google.android.libraries.identity.googleid:googleid:1.1.1")

    implementation("com.google.maps.android:maps-compose:4.3.3")
    // ↑ Componente GoogleMap() para Jetpack Compose.
    // Sin esto, tendrías que usar el MapView clásico (XML).

    implementation("com.google.android.gms:play-services-maps:19.0.0")
    // ↑ El SDK base de Google Maps. maps-compose lo usa internamente.

    implementation("com.google.android.gms:play-services-location:21.3.0")

    implementation("com.google.accompanist:accompanist-permissions:0.34.0")

    implementation("com.google.android.libraries.places:places:4.1.0")
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    // ↑ Usaremos Retrofit para llamar a la Directions API de Google
    // y obtener la polyline (ruta) entre origen y destino.

    // ── Stripe (pagos) ──
    implementation("com.stripe:stripe-android:22.6.0")

    // ── Firebase extras (managed by BoM) ──
    implementation("com.google.firebase:firebase-firestore")
    implementation("com.google.firebase:firebase-storage")
    implementation("com.google.firebase:firebase-messaging")

    // ── ViewModel Compose ──
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")

    // ── Coil (carga de imágenes) ──
    implementation("io.coil-kt:coil-compose:2.6.0")
}
