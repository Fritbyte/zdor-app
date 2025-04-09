package com.zdor.application.data.mapper

import com.zdor.application.data.model.output.ProfileResponse
import com.zdor.application.domain.model.Profile

fun ProfileResponse.toDomain(): Profile {
    return Profile(
        id = id,
        username = username,
        createdAt = created_at,
        updatedAt = updated_at
    )
} 