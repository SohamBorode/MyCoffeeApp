package com.example.mycoffeeapp.navigation

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mycoffeeapp.navigation.navbar.NavBarGraph
import com.example.mycoffeeapp.screens.welcomescreen.WelcomeScreen

@Composable
fun NavGraph() {
    val navControllerX = rememberNavController()

    // Change the NavHost transitions to this:
    NavHost(
        navController = navControllerX,
        startDestination = NavRoutes.WelcomeScreen,
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(700, easing = FastOutSlowInEasing)
            ) + fadeIn(animationSpec = tween(700))
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { -it / 3 },
                animationSpec = tween(700, easing = FastOutSlowInEasing)
            ) + fadeOut(animationSpec = tween(700))
        },
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { -it / 3 },
                animationSpec = tween(700, easing = FastOutSlowInEasing)
            ) + fadeIn(animationSpec = tween(700))
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(700, easing = FastOutSlowInEasing)
            ) + fadeOut(animationSpec = tween(700))
        }
    ) {
        composable<NavRoutes.WelcomeScreen> {
            WelcomeScreen(navControllerX)
        }
        composable<NavRoutes.NavBarGraph> {
            NavBarGraph(navControllerX)
        }
    }
}