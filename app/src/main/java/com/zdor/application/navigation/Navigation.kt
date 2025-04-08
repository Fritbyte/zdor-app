package com.zdor.application.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.zdor.application.presentation.screen.ConnectivityScreen
import com.zdor.application.presentation.screen.HomeScreen
import com.zdor.application.presentation.screen.LoginScreen
import com.zdor.application.presentation.screen.RegisterScreen
import com.zdor.application.presentation.viewmodel.AuthViewModel
import com.zdor.application.presentation.viewmodel.factory.AuthViewModelFactory

@Composable
private fun SetupAuthObserver(
    navController: NavController,
    authViewModel: AuthViewModel,
    loggedIn: Boolean
) {
    LaunchedEffect(loggedIn) {
        if (loggedIn) {
            authViewModel.refreshToken()
        }
    }

    LaunchedEffect(Unit) {
        val isAuthenticated = authViewModel.checkAuth()
        if (isAuthenticated) {
            authViewModel.refreshToken()
        }
    }
}

@Composable
fun Navigation(paddingValues: PaddingValues) {
    val context = LocalContext.current
    val authViewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(context))
    val navController = rememberNavController()
    val loginState by authViewModel.loginState.collectAsState()
    val loggedIn = loginState.token != null
    val coroutineScope = rememberCoroutineScope()

    SetupAuthObserver(navController, authViewModel, loggedIn)

    NavHost(
        navController = navController,
        startDestination = Screen.Connectivity.route,
        modifier = Modifier.padding(paddingValues)
    ) {
        composable(Screen.Connectivity.route) {
            ConnectivityScreen(navController)
        }

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
