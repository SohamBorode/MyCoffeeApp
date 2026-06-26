package com.example.mycoffeeapp.data.mapper

import com.example.mycoffeeapp.data.model.Account
import com.example.mycoffeeapp.data.model.Profile
import com.example.mycoffeeapp.data.model.dto.AccountDto
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

// Convert API model (DTO) to App model (Domain)
fun AccountDto.toDomainModel(): Account {
    return Account(
        username = this.username,
        fullName = this.fullName,
        email = this.email,
        dob = this.dob,
        phonNo = this.phonNo
    )
}

// Convert App model back to API model (for updates)
fun Account.toDto(): AccountDto {
    return AccountDto(
        username = this.username,
        email = this.email,
        phonNo = this.phonNo,
        dob = this.dob,
        fullName = this.fullName
    )
}