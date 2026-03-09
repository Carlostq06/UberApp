package com.example.uber_carlos.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.uber_carlos.ui.screens.*
import com.example.uber_carlos.viewmodel.AuthViewModel
import com.example.uber_carlos.viewmodel.PaymentViewModel
import com.example.uber_carlos.viewmodel.ProfileViewModel
import com.example.uber_carlos.viewmodel.RideViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()
    val rideViewModel: RideViewModel = viewModel()
    val profileViewModel: ProfileViewModel = viewModel()
    val paymentViewModel: PaymentViewModel = viewModel()

    val startDest = if (authViewModel.isLoggedIn) Routes.HOME_MAP else Routes.ONBOARDING

    NavHost(
        navController = navController,
        startDestination = startDest
    ) {
        composable(Routes.ONBOARDING) {
            MyScreen(onNavigateToLogin = { 
                navController.navigate(Routes.LOGIN) {
                    popUpTo(Routes.ONBOARDING) { inclusive = true }
                }
            })
        }

        composable(Routes.LOGIN) {
            LoginScreen(
                viewModel = authViewModel,
                onNavigateToCode = { navController.navigate(Routes.CODE) },
                onNavigateToHome = {
                    navController.navigate(Routes.HOME_MAP) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onNavigateToEmailLogin = {
                    navController.navigate(Routes.EMAIL_LOGIN)
                }
            )
        }

        composable(Routes.EMAIL_LOGIN) {
            EmailLoginScreen(
                viewModel = authViewModel,
                onBack = { navController.popBackStack() },
                onNavigateToRegister = { navController.navigate(Routes.EMAIL_REGISTER) },
                onSuccess = {
                    navController.navigate(Routes.HOME_MAP) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.EMAIL_REGISTER) {
            EmailRegisterScreen(
                viewModel = authViewModel,
                onBack = { navController.popBackStack() },
                onSuccess = {
                    navController.navigate(Routes.HOME_MAP) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.CODE) {
            ScreenCode(
                viewModel = authViewModel,
                onBack = { navController.popBackStack() },
                onSuccess = {
                    navController.navigate(Routes.SAFETY) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.SAFETY) {
            SafetyAndRespect(onNavigateToHome = {
                navController.navigate(Routes.HOME_MAP) {
                    popUpTo(Routes.SAFETY) { inclusive = true }
                }
            })
        }

        composable(Routes.HOME_MAP) {
            Home(
                authViewModel = authViewModel,
                rideViewModel = rideViewModel,
                onLogout = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.HOME_MAP) { inclusive = true }
                    }
                },
                onNavigateToMapSelection = {
                    navController.navigate(Routes.MAP_SELECTION)
                },
                onNavigateToProfile = {
                    navController.navigate(Routes.PROFILE)
                }
            )
        }

        composable(Routes.MAP_SELECTION) {
            MapSelectionScreen(
                onBack = { navController.popBackStack() },
                onDone = { navController.popBackStack() },
                rideViewModel = rideViewModel,
                paymentViewModel = paymentViewModel
            )
        }

        composable(Routes.PROFILE) {
            ProfileScreen(
                profileViewModel = profileViewModel,
                rideViewModel = rideViewModel,
                onBack = { navController.popBackStack() },
                onNavigateToHistory = { navController.navigate(Routes.HISTORY) }
            )
        }

        composable(Routes.HISTORY) {
            HistoryScreen(
                viewModel = rideViewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
