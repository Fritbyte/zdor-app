package com.zdor.application.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.zdor.application.presentation.screen.ConnectivityScreen
import com.zdor.application.presentation.screen.HomeScreen
import com.zdor.application.presentation.screen.LoginScreen
import com.zdor.application.presentation.screen.ProfileScreen
import com.zdor.application.presentation.screen.RegisterScreen
import com.zdor.application.presentation.viewmodel.AuthViewModel
import com.zdor.application.presentation.viewmodel.ProfileViewModel


@Composable
private fun SetupAuthObserver(
    navController: NavHostController,
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
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    authViewModel: AuthViewModel,
    isOnline: Boolean
) {
    val context = LocalContext.current
    val loginState by authViewModel.loginState.collectAsState()
    val loggedIn = loginState.token != null
    val coroutineScope = rememberCoroutineScope()

    SetupAuthObserver(navController, authViewModel, loggedIn)

    NavHost(
        navController = navController,
        startDestination = if (!isOnline) Screen.Connectivity.route else if (loggedIn) Screen.Home.route else Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(navController, authViewModel)
        }
        composable(Screen.Register.route) {
            RegisterScreen(navController, authViewModel)
        }
        composable(Screen.Connectivity.route) {
            ConnectivityScreen(navController)
        }
        composable(Screen.Home.route) {
            HomeScreen(navController, authViewModel)
        }
        composable(Screen.Profile.route) {
            val profileViewModel: ProfileViewModel = hiltViewModel()
            ProfileScreen(
                navController = navController,
                authViewModel = authViewModel,
                viewModel = profileViewModel
            )
        }
    }
}
