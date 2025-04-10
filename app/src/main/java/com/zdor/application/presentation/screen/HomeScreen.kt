package com.zdor.application.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.zdor.application.R
import com.zdor.application.presentation.components.HomeCard
import com.zdor.application.presentation.components.NetworkMonitor
import com.zdor.application.presentation.config.ColorResources
import com.zdor.application.presentation.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    LaunchedEffect(Unit) {
        authViewModel.refreshToken()
    }

    NetworkMonitor(navController = navController) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            ColorResources.homeBackground(),
                            ColorResources.white()
                        )
                    )
                )
        ) {
            Box(
                modifier = Modifier
                    .size(400.dp)
                    .offset(x = 150.dp, y = (-150).dp)
                    .alpha(0.5f)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                ColorResources.homeCircleBackground(),
                                ColorResources.homeCircleBackground().copy(alpha = 0.6f)
                            )
                        ),
                        CircleShape
                    )
                    .zIndex(0f)
            )

            Box(
                modifier = Modifier
                    .size(400.dp)
                    .offset(x = (-200).dp, y = 500.dp)
                    .alpha(0.5f)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                ColorResources.homeCircleBackground(),
                                ColorResources.homeCircleBackground().copy(alpha = 0.6f)
                            )
                        ),
                        CircleShape
                    )
                    .zIndex(0f)
            )

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                text = stringResource(R.string.app_name),
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold
                            )
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = ColorResources.transparent(),
                            titleContentColor = MaterialTheme.colorScheme.primary
                        )
                    )
                },
                containerColor = Color.Transparent
            ) { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(horizontal = 16.dp, vertical = 24.dp)
                        .zIndex(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    HomeCard(
                        title = stringResource(R.string.home_health_survey_title),
                        description = stringResource(R.string.home_health_survey_description),
                        icon = Icons.Default.Quiz,
                        onClick = { /* TODO: Навигация на экран опросника */ }
                    )

                    HomeCard(
                        title = stringResource(R.string.home_recommendations_title),
                        description = stringResource(R.string.home_recommendations_description),
                        icon = Icons.Default.LocalHospital,
                        onClick = { /* TODO: Навигация на экран рекомендаций */ }
                    )

                    HomeCard(
                        title = stringResource(R.string.home_education_title),
                        description = stringResource(R.string.home_education_description),
                        icon = Icons.Default.LibraryBooks,
                        onClick = { /* TODO: Навигация на экран материалов */ }
                    )
                }
            }
        }
    }
}
