package com.zdor.application.presentation.viewmodel

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NetworkViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _isOnline = MutableStateFlow(false)
    val isOnline: StateFlow<Boolean> = _isOnline

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private var networkCallback: ConnectivityManager.NetworkCallback? = null

    init {
        checkConnectionState()
        registerNetworkCallback()
    }

    private fun registerNetworkCallback() {
        networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                viewModelScope.launch {
                    _isOnline.value = true
                }
            }

            override fun onLost(network: Network) {
                viewModelScope.launch {
                    _isOnline.value = false
                }
            }
        }

        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        try {
            networkCallback?.let { callback ->
                connectivityManager.registerNetworkCallback(networkRequest, callback)
            }
        } catch (e: Exception) {
            _isOnline.value = false
        }
    }

    private fun checkConnectionState() {
        try {
            val network = connectivityManager.activeNetwork
            val capabilities = connectivityManager.getNetworkCapabilities(network)
            _isOnline.value =
                capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
        } catch (e: Exception) {
            _isOnline.value = false
        }
    }

    override fun onCleared() {
        super.onCleared()
        try {
            networkCallback?.let { callback ->
                connectivityManager.unregisterNetworkCallback(callback)
            }
        } catch (e: Exception) {
        }
    }
} 