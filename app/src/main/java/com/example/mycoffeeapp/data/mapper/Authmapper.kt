package com.example.mycoffeeapp.data.mapper

import com.example.mycoffeeapp.data.model.auth.AppUser
import com.example.mycoffeeapp.data.remote.auth.DemoUser

fun DemoUser.toAppUser() = AppUser(
    id = id,
    name = name,
    email = email
)