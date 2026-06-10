package com.example.mycoffeeapp.navigation.navbar

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mycoffeeapp.screens.cartscreen.CartScreen
import com.example.mycoffeeapp.screens.heartscreen.HeartScreen
import com.example.mycoffeeapp.screens.homeScreen.HomeScreen
import com.example.mycoffeeapp.screens.profilescreen.ProfileScreen

@Composable
fun NavBarGraph(navControllerX: NavHostController) {
    val navBarController = rememberNavController()

    NavHost(
        navController = navBarController,
        startDestination = NavBarRoutes.HomeScreen,

        enterTransition = {
            fadeIn(animationSpec = tween(400)) +
                    scaleIn(initialScale = 0.92f, animationSpec = tween(400))
        },
        exitTransition = {
            fadeOut(animationSpec = tween(300))
        },
        // Keep them simple for tabs to maintain responsiveness
        popEnterTransition = { fadeIn(animationSpec = tween(400)) },
        popExitTransition = { fadeOut(animationSpec = tween(400)) }
    ) {
        composable<NavBarRoutes.HomeScreen> {
            HomeScreen(navController = navBarController)
        }

        composable<NavBarRoutes.CartScreen> {
            CartScreen(navController = navBarController)
        }

        composable<NavBarRoutes.HeartScreen> {
            HeartScreen(navController = navBarController)
        }

        composable<NavBarRoutes.ProfileScreen> {
            ProfileScreen(navController = navBarController)
        }
    }
}