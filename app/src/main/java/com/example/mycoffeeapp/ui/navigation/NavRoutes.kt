package com.example.mycoffeeapp.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class NavRoutes {
    @Serializable
    data object WelcomeScreen : NavRoutes()

    @Serializable
    data object LoginScreen  : NavRoutes()

    @Serializable
    data object SignupScreen  : NavRoutes()

    @Serializable
    data object NavBarGraph : NavRoutes()
}