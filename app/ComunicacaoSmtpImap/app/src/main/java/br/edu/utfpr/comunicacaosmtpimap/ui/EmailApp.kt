package br.edu.utfpr.comunicacaosmtpimap.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import br.edu.utfpr.comunicacaosmtpimap.ui.screens.ComposeEmailScreen
import br.edu.utfpr.comunicacaosmtpimap.ui.screens.InboxScreen
import br.edu.utfpr.comunicacaosmtpimap.ui.screens.LoginScreen
import br.edu.utfpr.comunicacaosmtpimap.viewmodel.EmailViewModel

sealed class Screen(val route: String, val title: String, val icon: @Composable () -> Unit) {
    object Login : Screen("login", "Login", { Icon(Icons.Default.Settings, null) })
    object Inbox : Screen("inbox", "Inbox", { Icon(Icons.Default.List, null) })
    object Compose : Screen("compose", "Enviar", { Icon(Icons.Default.Email, null) })
}

@Composable
fun EmailApp() {
    val navController = rememberNavController()
    val viewModel: EmailViewModel = viewModel()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        bottomBar = {
            if (currentDestination?.route != Screen.Login.route) {
                NavigationBar {
                    val items = listOf(Screen.Inbox, Screen.Compose, Screen.Login)
                    items.forEach { screen ->
                        NavigationBarItem(
                            icon = screen.icon,
                            label = { Text(screen.title) },
                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            onClick = {
                                viewModel.resetState()
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController,
            startDestination = Screen.Login.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Login.route) {
                LoginScreen(viewModel) {
                    navController.navigate(Screen.Inbox.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            }
            composable(Screen.Inbox.route) {
                InboxScreen(viewModel)
            }
            composable(Screen.Compose.route) {
                ComposeEmailScreen(viewModel)
            }
        }
    }
}
