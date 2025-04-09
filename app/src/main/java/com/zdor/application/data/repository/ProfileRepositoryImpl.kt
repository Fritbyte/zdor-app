package com.zdor.application.data.repository

import com.zdor.application.data.api.ProfileApi
import com.zdor.application.data.mapper.toDomain
import com.zdor.application.data.model.output.ProfileResponse
import com.zdor.application.domain.model.Profile
import com.zdor.application.domain.repository.ProfileRepository
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileRepositoryImpl(private val profileApi: ProfileApi) : ProfileRepository {
    override fun getProfile(): Call<Profile> {
        lateinit var outerCall: Call<Profile>
        outerCall = object : Call<Profile> {
            override fun enqueue(callback: Callback<Profile>) {
                profileApi.getProfile().enqueue(object : Callback<ProfileResponse> {
                    override fun onResponse(
                        call: Call<ProfileResponse>,
                        response: Response<ProfileResponse>
                    ) {
                        if (response.isSuccessful) {
                            val body = response.body()?.toDomain()
                            callback.onResponse(outerCall, Response.success(body))
                        } else {
                            callback.onResponse(
                                outerCall,
                                Response.error(response.errorBody()!!, response.raw())
                            )
                        }
                    }

                    override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                        callback.onFailure(outerCall, t)
                    }
                })
            }

            override fun execute(): Response<Profile> = throw NotImplementedError()
            override fun clone(): Call<Profile> = getProfile()
            override fun isExecuted(): Boolean = false
            override fun cancel() {}
            override fun isCanceled(): Boolean = false
            override fun request(): Request = profileApi.getProfile().request()
            override fun timeout(): Timeout = profileApi.getProfile().timeout()
        }
        return outerCall
    }

    override fun updateProfile(profile: Profile): Call<Profile> {
        lateinit var outerCall: Call<Profile>
        outerCall = object : Call<Profile> {
            override fun enqueue(callback: Callback<Profile>) {
                profileApi.updateProfile(profile.id, profile)
                    .enqueue(object : Callback<ProfileResponse> {
                        override fun onResponse(
                            call: Call<ProfileResponse>,
                            response: Response<ProfileResponse>
                        ) {
                            if (response.isSuccessful) {
                                val body = response.body()?.toDomain()
                                callback.onResponse(outerCall, Response.success(body))
                            } else {
                                callback.onResponse(
                                    outerCall,
                                    Response.error(response.errorBody()!!, response.raw())
                                )
                            }
                        }

                        override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                            callback.onFailure(outerCall, t)
                        }
                    })
            }

            override fun execute(): Response<Profile> = throw NotImplementedError()
            override fun clone(): Call<Profile> = updateProfile(profile)
            override fun isExecuted(): Boolean = false
            override fun cancel() {}
            override fun isCanceled(): Boolean = false
            override fun request(): Request =
                profileApi.updateProfile(profile.id, profile).request()

            override fun timeout(): Timeout =
                profileApi.updateProfile(profile.id, profile).timeout()
        }
        return outerCall
    }
}
