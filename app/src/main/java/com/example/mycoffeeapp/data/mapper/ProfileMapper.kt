package com.example.mycoffeeapp.data.mapper

import com.example.mycoffeeapp.data.model.Profile
import com.example.mycoffeeapp.data.model.dto.ProfileDto

fun ProfileDto.toDomainModel(): Profile {
    return Profile(
        username = username,
        profileImageUri = profileImageUri
    )
}

fun Profile.toDto(): ProfileDto {
    return ProfileDto(
        username = username,
        profileImageUri = profileImageUri
    )
}
