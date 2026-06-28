package com.example.mycoffeeapp.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.mycoffeeapp.data.repository.coffeeItemList
import com.example.mycoffeeapp.di.ServiceLocator
import com.example.mycoffeeapp.hardware.camera.CameraPreview
import com.example.mycoffeeapp.ui.screens.cart.CartScreen
import com.example.mycoffeeapp.ui.screens.cart.CartUiState
import com.example.mycoffeeapp.ui.screens.cart.CartViewModel
import com.example.mycoffeeapp.ui.screens.favorite.FavoriteViewModel
import com.example.mycoffeeapp.ui.screens.favorite.HeartScreen
import com.example.mycoffeeapp.ui.screens.home.CoffeeDetailState
import com.example.mycoffeeapp.ui.screens.home.CoffeeDetailsScreen
import com.example.mycoffeeapp.ui.screens.home.HomeScreen
import com.example.mycoffeeapp.ui.screens.home.HomeUiState
import com.example.mycoffeeapp.ui.screens.home.HomeViewModel
import com.example.mycoffeeapp.ui.screens.loginsignp.AuthViewModel
import com.example.mycoffeeapp.ui.screens.profile.ProfileScreen
import com.example.mycoffeeapp.ui.screens.profile.ProfileViewModel

@Composable
fun NavBarGraph(rootNavController: NavHostController, authViewModel: AuthViewModel) {
    val navBarController = rememberNavController()

    val homeViewModel = remember {
        HomeViewModel(ServiceLocator.coffeeRepository, ServiceLocator.favoriteRepository)
    }
    val favoriteViewModel = remember {
        FavoriteViewModel(ServiceLocator.coffeeRepository, ServiceLocator.favoriteRepository)
    }
    val cartViewModel = remember { CartViewModel(ServiceLocator.cartRepository) }
    val cartState by cartViewModel.uiState.collectAsState()
    val cartCount = (cartState as? CartUiState.Success)
        ?.cartCoffeeList
        ?.sumOf { it.quantity }
        ?: 0

    val profileViewModel = remember { ProfileViewModel(ServiceLocator.profileRepository) }
    NavHost(
        navController = navBarController,
        startDestination = NavBarRoutes.HomeScreen,
        enterTransition = { fadeIn(animationSpec = tween(400)) + scaleIn(initialScale = 0.92f, animationSpec = tween(400)) },
        exitTransition = { fadeOut(animationSpec = tween(300)) },
        popEnterTransition = { fadeIn(animationSpec = tween(400)) },
        popExitTransition = { fadeOut(animationSpec = tween(400)) }
    ) {
        composable<NavBarRoutes.HomeScreen> {
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
                viewModel = profileViewModel,
                authViewModel = authViewModel,
                onLogoutSuccess = {
                    rootNavController.navigate(NavRoutes.WelcomeScreen) {
                        popUpTo(NavRoutes.NavBarGraph) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable<NavBarRoutes.CoffeeDetailsScreen> { backStackEntry ->
            val route = backStackEntry.toRoute<NavBarRoutes.CoffeeDetailsScreen>()
            val homeState by homeViewModel.uiState.collectAsState()
            val selectedCoffee = (if (homeState is HomeUiState.Success) {
                (homeState as HomeUiState.Success).coffeeList.find { it.id == route.coffeeId }
            } else null) ?: coffeeItemList.find { it.id == route.coffeeId } ?: coffeeItemList.first()

            CoffeeDetailsScreen(
                initialState = CoffeeDetailState(coffeeItem = selectedCoffee),
                onBackClick = { navBarController.popBackStack() },
                onFavoriteClick = { homeViewModel.toggleFavorite(selectedCoffee.id) },
                onAddToCartEndpoint = { _, size, temp, calculatedPrice ->
                    cartViewModel.addToCart(
                        coffeeItem = selectedCoffee,
                        size = size.name,
                        temperature = temp.name,
                        finalPrice = calculatedPrice
                    )
                }
            )
        }

        composable<NavBarRoutes.CameraPreview> {
            CameraPreview(navBarController = navBarController, viewModel = profileViewModel)
        }
    }
}


/*package com.example.mycoffeeapp.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.mycoffeeapp.di.ServiceLocator
import com.example.mycoffeeapp.data.repository.coffeeItemList
import com.example.mycoffeeapp.hardware.camera.CameraPreview
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
import com.example.mycoffeeapp.ui.screens.loginsignp.AuthViewModel
import com.example.mycoffeeapp.ui.screens.profile.ProfileScreen
import com.example.mycoffeeapp.ui.screens.profile.ProfileViewModel

@Composable
fun NavBarGraph() {
    val navBarController = rememberNavController()
    val homeViewModel = remember {
        HomeViewModel(ServiceLocator.coffeeRepository, ServiceLocator.favoriteRepository)
    }
    val favoriteViewModel = remember {
        FavoriteViewModel(ServiceLocator.coffeeRepository, ServiceLocator.favoriteRepository)
    }
    val cartViewModel = remember { CartViewModel(ServiceLocator.cartRepository) }
    val cartState by cartViewModel.uiState.collectAsState()
    val cartCount = (cartState as? CartUiState.Success)
        ?.cartCoffeeList
        ?.sumOf { it.quantity }
        ?: 0


    val profileViewModel = remember { ProfileViewModel(ServiceLocator.profileRepository) }

    val authViewModel = remember {
        AuthViewModel(
            authRepository = ServiceLocator.authRepository,
            sessionManager = ServiceLocator.sessionManager
        )
    }

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
                viewModel = profileViewModel,
                authViewModel = authViewModel
            )
        }

        composable<NavBarRoutes.CoffeeDetailsScreen> { backStackEntry ->
            val route = backStackEntry.toRoute<NavBarRoutes.CoffeeDetailsScreen>()

            val homeState by homeViewModel.uiState.collectAsState()
            val selectedCoffee = (if (homeState is HomeUiState.Success) {
                (homeState as HomeUiState.Success).coffeeList.find { it.id == route.coffeeId }
            } else null) ?: coffeeItemList.find { it.id == route.coffeeId } ?: coffeeItemList[0]

            CoffeeDetailsScreen(
                initialState = CoffeeDetailState(coffeeItem = selectedCoffee),
                onBackClick = { navBarController.popBackStack() },
                onFavoriteClick = { homeViewModel.toggleFavorite(selectedCoffee.id) },
                onAddToCartEndpoint = { _, size, temp, calculatedPrice ->
                    cartViewModel.addToCart(
                        coffeeItem = selectedCoffee,
                        size = size.name,
                        temperature = temp.name,
                        finalPrice = calculatedPrice
                    )
                }
            )
        }

        composable<NavBarRoutes.CameraPreview> { // Navigate directly to the preview
            CameraPreview(navBarController = navBarController, viewModel = profileViewModel)
        }
    }
}
*/
