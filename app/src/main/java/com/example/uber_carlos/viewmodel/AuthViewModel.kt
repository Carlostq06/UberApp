package com.example.uber_carlos.viewmodel

import android.app.Activity
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit

sealed interface AuthState{
    data object Idle: AuthState 
    data object Loading: AuthState 
    data class CodeSent(val verificationId: String): AuthState 
    data object Authenticated: AuthState 
    data class Error(val message: String): AuthState 
}

class AuthViewModel : ViewModel(){

    private val auth = FirebaseAuth.getInstance()
    var authState: AuthState by mutableStateOf(AuthState.Idle)
        private set

    val currentUser: FirebaseUser? get() = auth.currentUser 
    val isLoggedIn: Boolean get() = currentUser != null

    var phoneNumber by mutableStateOf("")
        private set

    fun onPhoneChanged(newValue: String) {
        phoneNumber = newValue
    }

    fun resetAuthState() {
        authState = AuthState.Idle
    }

    fun sendCode(activity: Activity) {
        if (phoneNumber.length < 7) {
            authState= AuthState.Error("Invalid phone number")
            return
        }
        authState = AuthState.Loading
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    signInWithCredential(credential)
                }
                override fun onVerificationFailed(e: FirebaseException) {
                    authState = AuthState.Error(e.message ?: "Verification failed")
                }
                override fun onCodeSent(id: String, token: PhoneAuthProvider.ForceResendingToken) {
                    authState = AuthState.CodeSent(id)
                }
            })
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun verifyOtp(otpCode: String) {
        val currentState = authState
        if (currentState is AuthState.CodeSent) {
            val credential = PhoneAuthProvider.getCredential(currentState.verificationId, otpCode)
            signInWithCredential(credential)
        }
    }

    private fun signInWithCredential(credential: PhoneAuthCredential) {
        authState = AuthState.Loading
        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                authState = AuthState.Authenticated
            } else {
                authState = AuthState.Error("Invalid code")
            }
        }
    }

    // ═══════════════════════════════════════════
    //  REGISTRAR usuario nuevo (email/contraseña)
    // ═══════════════════════════════════════════
    fun register(email: String, password: String) {
        authState = AuthState.Loading
        viewModelScope.launch {
            try {
                auth.createUserWithEmailAndPassword(email, password).await()
                authState = AuthState.Authenticated

            } catch (e: FirebaseAuthUserCollisionException) {
                authState = AuthState.Error("Este correo ya está registrado. Por favor, inicia sesión con Google.")

            } catch (e: Exception) {
                authState = AuthState.Error(e.localizedMessage ?: "Error al registrar")
            }
        }
    }

    // ═══════════════════════════════════════════
    //  LOGIN con usuario existente (email/contraseña)
    // ═══════════════════════════════════════════
    fun login(email: String, password: String) {
        authState = AuthState.Loading
        viewModelScope.launch {
            try {
                auth.signInWithEmailAndPassword(email, password).await()
                authState = AuthState.Authenticated
            } catch (e: Exception) {
                authState = AuthState.Error(e.localizedMessage ?: "Error al iniciar sesión")
            }
        }
    }

    companion object {
        private const val WEB_CLIENT_ID = "923345072334-kb1alb0bnvmuui7fdhdehlap6pl87se1.apps.googleusercontent.com"
    }

    fun signInWithGoogle(context: Context) {
        authState = AuthState.Loading
        viewModelScope.launch {
            try {
                val credentialManager = CredentialManager.create(context)
                val googleIdOption = GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(WEB_CLIENT_ID)
                    .build()
                val request = GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .build()
                val result = credentialManager.getCredential(request = request, context = context)
                val credential = result.credential
                if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    val googleIdToken = GoogleIdTokenCredential.createFrom(credential.data).idToken
                    val firebaseCredential = GoogleAuthProvider.getCredential(googleIdToken, null)
                    auth.signInWithCredential(firebaseCredential).await()
                    authState = AuthState.Authenticated
                } else {
                    authState = AuthState.Error("Unexpected credential type")
                }
            } catch (e: Exception) {
                authState = AuthState.Error(e.localizedMessage ?: "Error signing in with Google")
            }
        }
    }

    fun logout() {
        auth.signOut()
        authState = AuthState.Idle
    }
}
