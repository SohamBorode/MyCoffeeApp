package com.example.mycoffeeapp.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.mycoffeeapp.data.remote.favorite.DemoFavoriteDataSource
import com.example.mycoffeeapp.data.remote.favorite.RemoteFavoriteDataSource
import com.example.mycoffeeapp.data.remote.profile.DemoProfileDataSource
import com.example.mycoffeeapp.data.remote.profile.RemoteProfileDataSource
import com.example.mycoffeeapp.data.repository.CoffeeRepository
import com.example.mycoffeeapp.data.repository.cart.CartRepository
import com.example.mycoffeeapp.data.repository.coffeeItemList
import com.example.mycoffeeapp.data.repository.favorite.FavoriteRepository
import com.example.mycoffeeapp.data.repository.profile.ProfileRepository
import com.example.mycoffeeapp.ui.screens.cart.CartScreen
import com.example.mycoffeeapp.ui.screens.cart.CartViewModel
import com.example.mycoffeeapp.ui.screens.cart.CartUiState
import com.example.mycoffeeapp.ui.screens.favorite.FavoriteViewModel
import com.example.mycoffeeapp.ui.screens.favorite.HeartScreen
import com.example.mycoffeeapp.ui.screens.home.CoffeeDetailState
import com.example.mycoffeeapp.ui.screens.home.CoffeeDetailsScreen
import com.example.mycoffeeapp.ui.screens.home.HomeScreen
import com.example.mycoffeeapp.ui.screens.home.HomeUiState
import com.example.mycoffeeapp.ui.screens.home.HomeViewModel
import com.example.mycoffeeapp.ui.screens.profile.ProfileScreen
import com.example.mycoffeeapp.ui.screens.profile.ProfileViewModel

@Composable
fun NavBarGraph(navControllerX: NavHostController) {
    val navBarController = rememberNavController()
    val apiService = RetrofitClient.apiService
    val homeRepository = remember { CoffeeRepository(apiService) }
    val favoriteRepository = remember {
        FavoriteRepository(
            remote = RemoteFavoriteDataSource(RetrofitClient.favoriteApiService),
            demo = DemoFavoriteDataSource()
        )
    }
    val homeViewModel = remember { HomeViewModel(homeRepository, favoriteRepository) }
    val favoriteViewModel = remember { FavoriteViewModel(homeRepository, favoriteRepository) }
    val cartRepository = remember {
        CartRepository(
            remote = RemoteClassDataSource(RetrofitClient.cartRemoteApiService),
            demo = DemoCartDataSource()
        )
    }


    val cartViewModel = remember { CartViewModel(cartRepository) }
    val cartState by cartViewModel.uiState.collectAsState()
    val cartCount = (cartState as? CartUiState.Success)
        ?.cartCoffeeList
        ?.sumOf { it.quantity }
        ?: 0


    // profile page

    val profileRepository = remember {
        ProfileRepository(
            remote = RemoteProfileDataSource(RetrofitClient.profileApiService),
            demo = DemoProfileDataSource()
        )
    }
    val profileViewModel = remember { ProfileViewModel(profileRepository) }

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
/*
            // //           val viewModel: HomeViewModel = viewModel()
//
//            // Manual Solution 1 : val repository = CoffeeRepository(apiService)
//            val apiService = RetrofitClient.apiService
//            val repository = CoffeeRepository(apiService)
//            val viewModel = remember { HomeViewModel(repository) }

           */
            HomeScreen(
                navController = navBarController,
                homeViewModel = homeViewModel,
                favoriteViewModel = favoriteViewModel,
                cartCount = cartCount,
                onAddToCartClick = { coffeeItem ->
                    cartViewModel.addToCart(
                        coffeeItem = coffeeItem,
                        temperature = coffeeItem.temperature,
                        size = coffeeItem.size,
                        finalPrice = coffeeItem.price
                    )
                }
            )
        }
        /*
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
        */
        composable<NavBarRoutes.CartScreen> {
            CartScreen(
                navController = navBarController,
                onBackClick = { navBarController.popBackStack() },
                viewModel = cartViewModel,
                cartCount = cartCount
            )
        }

        composable<NavBarRoutes.HeartScreen> {
            HeartScreen(
                navController = navBarController,
                viewModel = favoriteViewModel,
                onAddToCartClick = { coffeeItem ->
                    cartViewModel.addToCart(
                        coffeeItem = coffeeItem,
                        temperature = coffeeItem.temperature,
                        size = coffeeItem.size,
                        finalPrice = coffeeItem.price
                    )
                }
            )
        }

        composable<NavBarRoutes.ProfileScreen> {
            ProfileScreen(
                navController = navBarController,
                viewModel = profileViewModel
            )
        }

        composable<NavBarRoutes.CoffeeDetailsScreen> { backStackEntry ->
            val route = backStackEntry.toRoute<NavBarRoutes.CoffeeDetailsScreen>()

            /*
             val homeViewModel: HomeViewModel = viewModel(
                 navControllerX.getBackStackEntry<NavBarRoutes.HomeScreen>()
             )*/


            val homeState by homeViewModel.uiState.collectAsState()
            val selectedCoffee = (if (homeState is HomeUiState.Success) {
                (homeState as HomeUiState.Success).coffeeList.find { it.id == route.coffeeId }
            } else null) ?: coffeeItemList.find { it.id == route.coffeeId } ?: coffeeItemList[0]

            /*val cartRemoteApi = RetrofitClient.cartRemoteApiService
            val cartRemoteService = RemoteClassDataSource(cartRemoteApi)
            val cartDemoService = DemoCartDataSource()
            val cartRepo = CartRepository(
                remote = cartRemoteService,
                demo = cartDemoService
            )
            val cartViewModel = remember { CartViewModel(cartRepo) }
*/
            CoffeeDetailsScreen(
                initialState = CoffeeDetailState(coffeeItem = selectedCoffee),
                onBackClick = { navBarController.popBackStack() },
                onFavoriteClick = { homeViewModel.toggleFavorite(selectedCoffee.id) },
                onAddToCartEndpoint = { id, size, temp, calculatedPrice ->
                    cartViewModel.addToCart(
                        coffeeItem = selectedCoffee,
                        size = size.name,
                        temperature = temp.name,
                        finalPrice = calculatedPrice
                    )
                }
            )
        }
    }
}
