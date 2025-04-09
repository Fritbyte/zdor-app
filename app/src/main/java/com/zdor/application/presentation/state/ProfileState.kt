package com.zdor.application.presentation.state

import com.zdor.application.domain.model.Profile

sealed class ProfileState {
    object Loading : ProfileState()
    data class Success(val profile: Profile) : ProfileState()
    data class Error(val message: String) : ProfileState()
} 