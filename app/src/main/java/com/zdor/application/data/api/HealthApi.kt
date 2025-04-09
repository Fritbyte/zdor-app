package com.zdor.application.data.api

import com.zdor.application.data.model.output.HealthResponse
import retrofit2.Call
import retrofit2.http.GET

interface HealthApi {
    @GET("/api/health")
    fun checkHealth(): Call<HealthResponse>
} 