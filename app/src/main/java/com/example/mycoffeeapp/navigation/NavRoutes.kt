package com.example.mycoffeeapp.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class NavRoutes {
    @Serializable
    data object WelcomeScreen : NavRoutes()

    @Serializable
    data object NavBarGraph : NavRoutes()
}