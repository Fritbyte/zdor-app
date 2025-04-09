package com.zdor.application.presentation.components

import androidx.compose.runtime.Composable

data class NavigationItem(
    val route: String,
    val icon: @Composable () -> Unit,
    val labelResId: Int
)
