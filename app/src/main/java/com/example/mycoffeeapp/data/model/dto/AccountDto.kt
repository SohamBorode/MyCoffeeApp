package com.example.mycoffeeapp.data.model.dto

import com.example.mycoffeeapp.R

data class AccountDto(
    val username: String,
    var fullName: String,
    val email: String,
    val dob: String,
    val phonNo: String,
//    val passwordReset: String,
//    val profileImageUri: String? = null,
//    val defaultProfileImage: Int = R.drawable.coffee_5
)
