package com.example.mycoffeeapp.ui.navigation

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

    @Serializable
    data class CoffeeDetailsScreen(val coffeeId : String) : NavBarRoutes()

    @Serializable
    data object CameraPreview: NavBarRoutes()
}