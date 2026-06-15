package com.example.mycoffeeapp.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mycoffeeapp.data.remote.RetrofitClient
import com.example.mycoffeeapp.data.remote.cart.DemoCartDataSource
import com.example.mycoffeeapp.data.remote.cart.RemoteClassDataSource
import com.example.mycoffeeapp.data.repository.CoffeeRepository
import com.example.mycoffeeapp.data.repository.cart.CartRepository
import com.example.mycoffeeapp.data.repository.coffeeItemList
import com.example.mycoffeeapp.ui.screens.cart.CartScreen
import com.example.mycoffeeapp.ui.screens.cart.CartViewModel
import com.example.mycoffeeapp.ui.screens.favorite.HeartScreen
import com.example.mycoffeeapp.ui.screens.home.CoffeeDetailState
import com.example.mycoffeeapp.ui.screens.home.CoffeeDetailsScreen
import com.example.mycoffeeapp.ui.screens.home.HomeScreen
import com.example.mycoffeeapp.ui.screens.home.HomeViewModel
import com.example.mycoffeeapp.ui.screens.profile.ProfileScreen

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
        popEnterTransition = { fadeIn(animationSpec = tween(400)) },
        popExitTransition = { fadeOut(animationSpec = tween(400)) }

    ) {
        composable<NavBarRoutes.HomeScreen> {
//            val viewModel: HomeViewModel = viewModel()

            // Manual Solution 1 : val repository = CoffeeRepository(apiService)
            val apiService = RetrofitClient.apiService
            val repository = CoffeeRepository(apiService)
            val viewModel = remember { HomeViewModel(repository) }
            HomeScreen(navController = navBarController, viewModel = viewModel)
        }

        composable<NavBarRoutes.CartScreen> {


            val remoteApi = RetrofitClient.cartRemoteApiService
            val carRemoteService = RemoteClassDataSource(remoteApi)
            val cartDemoService = DemoCartDataSource()
            val cartRepo = CartRepository(
                remote = carRemoteService,
                demo = cartDemoService
            )

            val viewModel = remember { CartViewModel(cartRepo) }

            CartScreen(
                navController = navBarController,
                onBackClick = { navBarController.popBackStack() },
                viewModel = viewModel
            )
        }

        composable<NavBarRoutes.HeartScreen> {
            HeartScreen(navController = navBarController)
        }

        composable<NavBarRoutes.ProfileScreen> {
            ProfileScreen(navController = navBarController)
        }

        composable<NavBarRoutes.CoffeeDetailsScreen> { backStackEntry ->
            val route = backStackEntry.toRoute<NavBarRoutes.CoffeeDetailsScreen>()
            val selectedCoffee =
                coffeeItemList.find { it.id == route.coffeeId } ?: coffeeItemList[0]

            CoffeeDetailsScreen(
                initialState = CoffeeDetailState(coffeeItem = selectedCoffee),
                onBackClick = { navBarController.popBackStack() },
                onFavoriteClick = { /* logic */ },
                onAddToCartEndpoint = { id, size, temp, price ->
                    println("Added $id to cart")
                }
            )
        }
    }
}