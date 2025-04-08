package com.zdor.application.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zdor.application.data.api.ApiConfig
import com.zdor.application.data.api.Connectivity
import com.zdor.application.data.model.output.HealthResponse
import com.zdor.application.presentation.state.ConnectivityState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Call

class ConnectivityViewModel : ViewModel() {
    private val api = ApiConfig.retrofit.create(Connectivity::class.java)

    private val _connectivityState = MutableStateFlow(ConnectivityState())
    val connectivityState: StateFlow<ConnectivityState> = _connectivityState

    fun checkServerConnectivity() {
        _connectivityState.value = ConnectivityState(isChecking = true)

        api.checkHealth().enqueue(object : Callback<HealthResponse> {
            override fun onResponse(call: Call<HealthResponse>, response: Response<HealthResponse>) {
                if (response.isSuccessful && response.body()?.status == "ok") {
                    _connectivityState.value = ConnectivityState(isConnected = true)
                } else {
                    _connectivityState.value = ConnectivityState(
                        isConnected = false,
                        error = "Сервер недоступен: ${response.code()}"
                    )
                    startPeriodicConnectivityCheck()
                }
            }

            override fun onFailure(call: Call<HealthResponse>, t: Throwable) {
                _connectivityState.value = ConnectivityState(
                    isConnected = false,
                    error = "Ошибка подключения: ${t.message ?: "Неизвестная ошибка"}"
                )
                startPeriodicConnectivityCheck()
            }
        })
    }

    private fun startPeriodicConnectivityCheck() {
        viewModelScope.launch {
            delay(5000)
            checkServerConnectivity()
        }
    }
}
