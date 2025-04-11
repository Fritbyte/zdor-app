package com.zdor.application.navigation

sealed class Screen(val route: String) {
    object Profile : Screen("profile")
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object Connectivity : Screen("connectivity")
    object Education : Screen("education")
    object Recommendations : Screen("recommendations")
}