package com.zdor.application.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.zdor.application.data.api.HealthApi
import com.zdor.application.data.model.output.HealthResponse
import com.zdor.application.data.network.NetworkStateHolder
import com.zdor.application.presentation.state.ConnectivityState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConnectivityViewModel @Inject constructor(
    private val api: HealthApi
) : ViewModel() {

    private val _connectivityState = MutableStateFlow(ConnectivityState())
    val connectivityState: StateFlow<ConnectivityState> = _connectivityState

    private var periodicCheckJob: Job? = null

    fun checkServerConnectivity() {
        _connectivityState.value = ConnectivityState(isChecking = true)
        api.checkHealth().enqueue(object : retrofit2.Callback<HealthResponse> {
            override fun onResponse(
                call: retrofit2.Call<HealthResponse>,
                response: retrofit2.Response<HealthResponse>
            ) {
                val isConnected = response.isSuccessful
                NetworkStateHolder.updateConnectionState(isConnected)
                _connectivityState.value = ConnectivityState(isConnected = isConnected)
                if (!isConnected) {
                    _connectivityState.value = ConnectivityState(
                        isConnected = false,
                        error = "Сервер недоступен"
                    )
                }
                startPeriodicConnectivityCheck()
            }

            override fun onFailure(call: retrofit2.Call<HealthResponse>, t: Throwable) {
                NetworkStateHolder.updateConnectionState(false)
                _connectivityState.value = ConnectivityState(
                    isConnected = false,
                    error = t.message ?: "Ошибка подключения"
                )
                startPeriodicConnectivityCheck()
            }
        })
    }

    private fun startPeriodicConnectivityCheck() {
        periodicCheckJob?.cancel()
        periodicCheckJob = CoroutineScope(Dispatchers.IO).launch {
            delay(30000)
            checkServerConnectivity()
        }
    }

    override fun onCleared() {
        super.onCleared()
        periodicCheckJob?.cancel()
    }
}
