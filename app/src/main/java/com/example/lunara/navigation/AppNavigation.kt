package com.example.lunara.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.lunara.screens.MainScreen
import com.example.lunara.screens.OnboardingScreen
import com.example.lunara.screens.SplashScreen
import com.example.lunara.viewmodel.OnboardingViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await


@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    var isFirebaseReady by remember { mutableStateOf(false) }
    var startDestination by remember { mutableStateOf("splash") } // Inicia en Splash

    // Efecto para iniciar sesión anónimamente al arrancar
    LaunchedEffect(Unit) {
        try {
            val auth = Firebase.auth
            val user = auth.currentUser
            if (user == null) {
                auth.signInAnonymously().await()
                Log.d("AppNavigation", "Login anónimo exitoso.")
            } else {
                Log.d("AppNavigation", "Usuaria ya estaba logueada: ${user.uid}")
            }

            // --- LÓGICA DE NAVEGACIÓN ---
            // Una vez logueados, revisamos si la usuaria ya tiene un perfil
            val uid = auth.currentUser!!.uid
            val db = Firebase.firestore
            val userProfileDoc = db.collection("users").document(uid).get().await()

            startDestination = if (userProfileDoc.exists()) {
                // Si el perfil existe, vamos directo a la app principal
                "main_app"
            } else {
                // Si no existe, vamos al onboarding
                "onboarding"
            }

            isFirebaseReady = true // Firebase está listo y sabemos dónde navegar

        } catch (e: Exception) {
            Log.e("AppNavigation", "Error en el login anónimo o al verificar perfil", e)
            // Aquí podrías mostrar un error persistente
        }
    }

    NavHost(navController = navController, startDestination = startDestination) {
        // Definimos la pantalla splash primero
        composable("splash") {
            SplashScreen()
        }

        composable("onboarding") {
            val onboardingViewModel: OnboardingViewModel = viewModel()
            OnboardingScreen(
                viewModel = onboardingViewModel,
                onOnboardingComplete = {
                    navController.navigate("main_app") {
                        popUpTo("onboarding") { inclusive = true }
                    }
                }
            )
        }
        composable("main_app") {
            MainScreen(
                onSignOut = {
                    navController.navigate("splash") {
                        // Borramos todo el stack de navegación de la app
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                }
            )
        }
    }

    // Este LaunchedEffect reacciona al cambio de startDestination
    // y navega una vez que Firebase está listo.
    LaunchedEffect(isFirebaseReady, startDestination) {
        if (isFirebaseReady && startDestination != "splash") {
            navController.navigate(startDestination) {
                popUpTo("splash") { inclusive = true } // Salimos del splash
            }
        }
    }
}