package com.zdor.application.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.zdor.application.presentation.viewmodel.AuthViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val token = authViewModel.loginState.collectAsState().value.token

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Добро пожаловать!\nToken: ${token ?: "Нет токена"}")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            authViewModel.logout()
        }) {
            Text("Выйти")
        }
    }
}
