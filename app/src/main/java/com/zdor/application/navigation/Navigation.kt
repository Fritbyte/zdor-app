package com.zdor.application.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.zdor.application.presentation.screen.HomeScreen
import com.zdor.application.presentation.screen.LoginScreen
import com.zdor.application.presentation.screen.RegisterScreen
import com.zdor.application.presentation.viewmodel.AuthViewModel
import com.zdor.application.presentation.viewmodel.factory.AuthViewModelFactory

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
}

@Composable
fun Navigation(paddingValues: PaddingValues) {
    val context = LocalContext.current
    val authViewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(context))
    val navController = rememberNavController()
    val loginState by authViewModel.loginState.collectAsState()
    val loggedIn = loginState.token != null

    NavHost(
        navController = navController,
        startDestination = if (loggedIn) Screen.Home.route else Screen.Login.route,
        modifier = Modifier.padding(paddingValues)
    ) {
        composable(Screen.Login.route) {
            if (loggedIn) {
                LaunchedEffect(Unit) {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            } else {
                LoginScreen(navController, authViewModel)
            }
        }
        composable(Screen.Register.route) {
            if (loggedIn) {
                LaunchedEffect(Unit) {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                }
            } else {
                RegisterScreen(navController, authViewModel)
            }
        }
        composable(Screen.Home.route) {
            if (!loggedIn) {
                LaunchedEffect(Unit) {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            } else {
                HomeScreen(navController, authViewModel)
            }
        }
    }
}
