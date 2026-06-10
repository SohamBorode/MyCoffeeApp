package com.example.mycoffeeapp.navigation.navbar

import kotlinx.serialization.Serializable

@Serializable
sealed class NavBarRoutes {
    @Serializable
    data object HomeScreen : NavBarRoutes()

    @Serializable
    data object CartScreen : NavBarRoutes()

    @Serializable
    data object HeartScreen : NavBarRoutes()

    @Serializable
    data object ProfileScreen : NavBarRoutes()
}