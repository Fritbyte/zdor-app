package com.zdor.application

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.zdor.application.navigation.AppNavigation
import com.zdor.application.presentation.viewmodel.AuthViewModel
import com.zdor.application.presentation.viewmodel.NetworkViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        
        setContent {
            MaterialTheme {
                Surface {
                    val navController = rememberNavController()
                    val authViewModel: AuthViewModel = hiltViewModel()
                    val networkViewModel: NetworkViewModel = hiltViewModel()
                    val isOnline by networkViewModel.isOnline.collectAsState()

                    AppNavigation(
                        navController = navController,
                        authViewModel = authViewModel,
                        isOnline = isOnline
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        window.decorView.rootView?.clearFocus()
        super.onDestroy()
    }
}
