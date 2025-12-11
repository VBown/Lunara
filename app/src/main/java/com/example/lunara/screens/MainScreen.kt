package com.example.lunara.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.lunara.ui.theme.BlancoHueso
import com.example.lunara.viewmodel.DashboardViewModel


// --- PANTALLA PRINCIPAL CON NAVEGACIÓN INFERIOR ---
@Composable
fun MainScreen(onSignOut: () -> Unit) {
    val navController = rememberNavController()
    Scaffold(bottomBar = { BottomNavigationBar(navController) }) { padding ->
        NavHost(navController = navController, startDestination = "dashboard", modifier = Modifier.padding(padding)) {
            composable("dashboard") {
                val vm: DashboardViewModel = viewModel()
                val entry by navController.currentBackStackEntryAsState()
                LaunchedEffect(entry) { if (entry?.destination?.route == "dashboard") vm.refreshDashboardContent() }
                DashboardScreen(vm)
            }
            composable("plan") { PlanScreen(viewModel()) }
            composable("tracking") { TrackingScreen(viewModel()) }
            composable("profile") { ProfileScreen(viewModel(), onSignOut) }
            composable("chatbot") { ChatbotScreen(viewModel()) }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        Triple("dashboard", Icons.Default.Dashboard, "Dashboard"),
        Triple("plan", Icons.Default.Assignment, "Plan"),
        Triple("tracking", Icons.Default.DateRange, "Registro"),
        Triple("profile", Icons.Default.Person, "Perfil"),
        Triple("chatbot", Icons.Default.ChatBubble, "Chatbot")
    )
    val currentEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentEntry?.destination?.route

    NavigationBar(containerColor = BlancoHueso) {
        items.forEach { (route, icon, label) ->
            NavigationBarItem(
                icon = { Icon(icon,
                    contentDescription = label) },
                label = { Text(label,
                    style = MaterialTheme.typography.labelMedium) },
                selected = currentRoute == route,
                onClick = { navController.navigate(route)
                { popUpTo(navController.graph.startDestinationId)
                { saveState = true }
                    launchSingleTop = true
                    restoreState = true } },
                colors = NavigationBarItemDefaults
                    .colors(selectedIconColor = MaterialTheme
                        .colorScheme.primary,
                        indicatorColor = MaterialTheme
                            .colorScheme.primary
                            .copy(alpha = 0.1f))
            )
        }
    }
}
