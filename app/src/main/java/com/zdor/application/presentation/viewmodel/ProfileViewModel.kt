package com.zdor.application.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zdor.application.domain.model.Profile
import com.zdor.application.domain.repository.ProfileRepository
import com.zdor.application.domain.repository.TokenRepository
import com.zdor.application.presentation.state.ProfileState
import com.zdor.application.util.DateFormatter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val tokenRepository: TokenRepository
) : ViewModel() {

    private val _profileState = MutableLiveData<ProfileState>()
    val profileState: LiveData<ProfileState> = _profileState

    fun loadProfile() {
        viewModelScope.launch {
            _profileState.value = ProfileState.Loading
            
            val token = tokenRepository.getToken()
            if (token == null) {
                _profileState.value = ProfileState.Error("Необходима авторизация")
                return@launch
            }

            try {
                profileRepository.getProfile().enqueue(object : Callback<Profile> {
                    override fun onResponse(call: Call<Profile>, response: Response<Profile>) {
                        if (response.isSuccessful && response.body() != null) {
                            val profile = response.body()!!
                            val formattedProfile = profile.copy(
                                createdAt = DateFormatter.formatDateTitle(profile.createdAt),
                                updatedAt = DateFormatter.formatDateTitle(profile.updatedAt)
                            )
                            _profileState.value = ProfileState.Success(formattedProfile)
                        } else {
                            when (response.code()) {
                                401 -> _profileState.value = ProfileState.Error("Необходима повторная авторизация")
                                403 -> _profileState.value = ProfileState.Error("Доступ запрещен")
                                404 -> _profileState.value = ProfileState.Error("Профиль не найден")
                                else -> _profileState.value = ProfileState.Error("Ошибка загрузки профиля")
                            }
                        }
                    }

                    override fun onFailure(call: Call<Profile>, t: Throwable) {
                        _profileState.value = ProfileState.Error(t.message ?: "Неизвестная ошибка")
                    }
                })
            } catch (e: Exception) {
                _profileState.value = ProfileState.Error("Ошибка при загрузке профиля: ${e.message}")
            }
        }
    }

    fun retryLoadProfile() {
        loadProfile()
    }
} 