package com.zdor.application.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import com.zdor.application.data.network.NetworkStateHolder
import com.zdor.application.navigation.Screen

@Composable
fun NetworkMonitor(
    navController: NavController,
    content: @Composable () -> Unit
) {
    val isConnected by NetworkStateHolder.isConnected.collectAsState()

    LaunchedEffect(isConnected) {
        if (!isConnected) {
            navController.navigate(Screen.Connectivity.route) {
                popUpTo(navController.graph.startDestinationId) {
                    saveState = true
                }
                launchSingleTop = true
            }
        }
    }

    content()
} 