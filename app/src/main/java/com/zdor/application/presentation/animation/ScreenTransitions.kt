package com.zdor.application.presentation.animation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavBackStackEntry

object ScreenTransitions {
    fun enterTransition(backStackEntry: NavBackStackEntry): EnterTransition {
        return slideInHorizontally(
            animationSpec = tween(300),
            initialOffsetX = { fullWidth -> fullWidth }
        ) + fadeIn(animationSpec = tween(300))
    }

    fun exitTransition(backStackEntry: NavBackStackEntry): ExitTransition {
        return slideOutHorizontally(
            animationSpec = tween(300),
            targetOffsetX = { fullWidth -> -fullWidth }
        ) + fadeOut(animationSpec = tween(300))
    }

    fun popEnterTransition(backStackEntry: NavBackStackEntry): EnterTransition {
        return slideInHorizontally(
            animationSpec = tween(300),
            initialOffsetX = { fullWidth -> -fullWidth }
        ) + fadeIn(animationSpec = tween(300))
    }

    fun popExitTransition(backStackEntry: NavBackStackEntry): ExitTransition {
        return slideOutHorizontally(
            animationSpec = tween(300),
            targetOffsetX = { fullWidth -> fullWidth }
        ) + fadeOut(animationSpec = tween(300))
    }
} 