package com.zdor.application.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.zdor.application.presentation.animation.ScreenTransitions
import com.zdor.application.presentation.components.NavigationBar
import com.zdor.application.presentation.screen.*
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

    val startDestination = if (!isOnline) {
        Screen.Connectivity.route
    } else if (loggedIn) {
        Screen.Home.route
    } else {
        Screen.Login.route
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    
    val showBottomBar = remember(currentRoute) {
        when (currentRoute) {
            Screen.Home.route, Screen.Profile.route -> true
            else -> false
        }
    }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(navController = navController)
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(
                route = Screen.Login.route,
                enterTransition = { ScreenTransitions.enterTransition(this.targetState) },
                exitTransition = { ScreenTransitions.exitTransition(this.targetState) },
                popEnterTransition = { ScreenTransitions.popEnterTransition(this.initialState) },
                popExitTransition = { ScreenTransitions.popExitTransition(this.initialState) }
            ) {
                LoginScreen(navController, authViewModel)
            }
            
            composable(
                route = Screen.Register.route,
                enterTransition = { ScreenTransitions.enterTransition(this.targetState) },
                exitTransition = { ScreenTransitions.exitTransition(this.targetState) },
                popEnterTransition = { ScreenTransitions.popEnterTransition(this.initialState) },
                popExitTransition = { ScreenTransitions.popExitTransition(this.initialState) }
            ) {
                RegisterScreen(navController, authViewModel)
            }
            
            composable(
                route = Screen.Connectivity.route,
                enterTransition = { ScreenTransitions.enterTransition(this.targetState) },
                exitTransition = { ScreenTransitions.exitTransition(this.targetState) },
                popEnterTransition = { ScreenTransitions.popEnterTransition(this.initialState) },
                popExitTransition = { ScreenTransitions.popExitTransition(this.initialState) }
            ) {
                ConnectivityScreen(navController)
            }
            
            composable(
                route = Screen.Home.route,
                enterTransition = { ScreenTransitions.enterTransition(this.targetState) },
                exitTransition = { ScreenTransitions.exitTransition(this.targetState) },
                popEnterTransition = { ScreenTransitions.popEnterTransition(this.initialState) },
                popExitTransition = { ScreenTransitions.popExitTransition(this.initialState) }
            ) {
                HomeScreen(navController, authViewModel)
            }
            
            composable(
                route = Screen.Profile.route,
                enterTransition = { ScreenTransitions.enterTransition(this.targetState) },
                exitTransition = { ScreenTransitions.exitTransition(this.targetState) },
                popEnterTransition = { ScreenTransitions.popEnterTransition(this.initialState) },
                popExitTransition = { ScreenTransitions.popExitTransition(this.initialState) }
            ) {
                val profileViewModel: ProfileViewModel = hiltViewModel()
                ProfileScreen(
                    navController = navController,
                    authViewModel = authViewModel,
                    viewModel = profileViewModel
                )
            }
        }
    }
}
