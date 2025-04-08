package com.zdor.application.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.zdor.application.R
import com.zdor.application.navigation.Screen
import com.zdor.application.presentation.config.ColorResources
import com.zdor.application.presentation.viewmodel.AuthViewModel
import com.zdor.application.presentation.viewmodel.ConnectivityViewModel
import com.zdor.application.presentation.viewmodel.factory.AuthViewModelFactory

@Composable
fun ConnectivityScreen(navController: NavController) {
    val connectivityViewModel: ConnectivityViewModel = viewModel()
    val connectivityState by connectivityViewModel.connectivityState.collectAsState()
    val context = LocalContext.current
    val authViewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(context))
    val coroutineScope = rememberCoroutineScope()
    val loginState by authViewModel.loginState.collectAsState()

    LaunchedEffect(Unit) {
        connectivityViewModel.checkServerConnectivity()
    }

    if (connectivityState.isConnected) {
        LaunchedEffect(Unit) {
            val isAuthenticated = authViewModel.checkAuth()

            if (isAuthenticated) {
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Connectivity.route) { inclusive = true }
                }
            } else {
                navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.Connectivity.route) { inclusive = true }
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (connectivityState.isChecking) {
                CircularProgressIndicator(modifier = Modifier.size(50.dp))
                Spacer(modifier = Modifier.padding(16.dp))
                Text(text = stringResource(R.string.connecting), style = MaterialTheme.typography.bodyLarge)
            } else if (!connectivityState.isConnected) {
                Text(
                    text = stringResource(R.string.no_connection),
                    style = MaterialTheme.typography.bodyLarge,
                    color = ColorResources.red(),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
        }
    }
}