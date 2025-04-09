package com.zdor.application.data.api

import com.zdor.application.data.model.output.ProfileResponse
import com.zdor.application.domain.model.Profile
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface ProfileApi {
    @GET("/api/profile")
    fun getProfile(): Call<ProfileResponse>

    @PUT("/api/profile/{id}")
    fun updateProfile(@Path("id") id: Int, @Body profile: Profile): Call<ProfileResponse>
} 