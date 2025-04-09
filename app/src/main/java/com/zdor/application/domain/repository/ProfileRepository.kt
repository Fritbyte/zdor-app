package com.zdor.application.domain.repository

import com.zdor.application.domain.model.Profile
import retrofit2.Call

interface ProfileRepository {
    fun getProfile(): Call<Profile>
    fun updateProfile(profile: Profile): Call<Profile>
} 