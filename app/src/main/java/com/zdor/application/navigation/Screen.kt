package com.zdor.application.navigation

sealed class Screen(val route: String) {
    object Connectivity : Screen("connectivity")
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
}