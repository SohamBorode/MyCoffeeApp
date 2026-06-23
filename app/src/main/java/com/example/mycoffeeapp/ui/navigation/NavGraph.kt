package com.example.mycoffeeapp.ui.navigation

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mycoffeeapp.di.ServiceLocator
import com.example.mycoffeeapp.ui.screens.loginsignp.AuthViewModel
import com.example.mycoffeeapp.ui.screens.loginsignp.LoginScreen
import com.example.mycoffeeapp.ui.screens.loginsignp.SignupScreen
import com.example.mycoffeeapp.ui.screens.welcome.WelcomeScreen

@Composable
fun NavGraph() {
    val navControllerX = rememberNavController()

    val authViewModel = remember { AuthViewModel(ServiceLocator.authRepository) }
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

        composable<NavRoutes.LoginScreen> {
            LoginScreen(
                navControllerX,
                authViewModel = authViewModel
            )
        }

        composable<NavRoutes.SignupScreen> {
            SignupScreen(
                navControllerX,
                authViewModel = authViewModel
            )
        }


        composable<NavRoutes.NavBarGraph> {
            NavBarGraph()
        }
    }
}

