# Cart Badge Scalable Solution

This reference shows the refactor needed so the cart badge updates on every screen, not only when the Cart tab is opened.

## Goals
- Keep one `CartViewModel` for the whole bottom-bar flow.
- Observe cart state at the nav-graph/root level.
- Pass `cartCount` into `NavBarDesign` from shared state.
- Remove per-screen `CartViewModel` creation.

## 1) `CartViewModel.kt`
```kotlin
package com.example.mycoffeeapp.ui.screens.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mycoffeeapp.data.model.CoffeeItem
import com.example.mycoffeeapp.data.model.cart.CartItem
import com.example.mycoffeeapp.data.repository.cart.CartRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface CartUiState {
    data object Loading : CartUiState
    data class Success(val cartCoffeeList: List<CartItem>) : CartUiState
    data class Error(val msg: String) : CartUiState
}

class CartViewModel(
    private val cartRepository: CartRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<CartUiState>(CartUiState.Loading)
    val uiState: StateFlow<CartUiState> = _uiState.asStateFlow()

    val cartCount: Int
        get() = (uiState.value as? CartUiState.Success)?.cartCoffeeList?.size ?: 0

    init {
        loadCartData()
    }

    fun loadCartData() {
        viewModelScope.launch {
            _uiState.value = CartUiState.Loading
            runCatching { cartRepository.getCartItems() }
                .onSuccess { items ->
                    _uiState.value = CartUiState.Success(items)
                }
                .onFailure { err ->
                    _uiState.value = CartUiState.Error(err.localizedMessage ?: "Unknown network error")
                }
        }
    }

    fun addToCart(
        coffeeItem: CoffeeItem,
        size: String,
        temperature: String,
        finalPrice: Double = coffeeItem.price,
    ) {
        viewModelScope.launch {
            val cartItem = CartItem(
                id = java.util.UUID.randomUUID().toString(),
                coffeeId = coffeeItem.id,
                name = coffeeItem.name,
                imageUrl = coffeeItem.imageUrl,
                price = finalPrice,
                size = size,
                temperature = temperature,
                quantity = 1
            )
            cartRepository.addToCart(cartItem)
            loadCartData()
        }
    }

    fun deleteItem(id: String) {
        viewModelScope.launch {
            cartRepository.deleteCartItem(id)
            loadCartData()
        }
    }
}
```

## 2) `NavBarGraph.kt`
```kotlin
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
import com.example.mycoffeeapp.data.remote.RetrofitClient
import com.example.mycoffeeapp.data.remote.cart.DemoCartDataSource
import com.example.mycoffeeapp.data.remote.cart.RemoteClassDataSource
import com.example.mycoffeeapp.data.repository.CoffeeRepository
import com.example.mycoffeeapp.data.repository.cart.CartRepository
import com.example.mycoffeeapp.data.repository.coffeeItemList
import com.example.mycoffeeapp.ui.screens.cart.CartScreen
import com.example.mycoffeeapp.ui.screens.cart.CartUiState
import com.example.mycoffeeapp.ui.screens.cart.CartViewModel
import com.example.mycoffeeapp.ui.screens.favorite.HeartScreen
import com.example.mycoffeeapp.ui.screens.home.CoffeeDetailState
import com.example.mycoffeeapp.ui.screens.home.CoffeeDetailsScreen
import com.example.mycoffeeapp.ui.screens.home.HomeScreen
import com.example.mycoffeeapp.ui.screens.home.HomeUiState
import com.example.mycoffeeapp.ui.screens.home.HomeViewModel
import com.example.mycoffeeapp.ui.screens.profile.ProfileScreen

@Composable
fun NavBarGraph(navControllerX: NavHostController) {
    val navBarController = rememberNavController()

    val apiService = RetrofitClient.apiService
    val repository = remember { CoffeeRepository(apiService) }
    val homeViewModel = remember { HomeViewModel(repository) }

    val cartRepository = remember {
        CartRepository(
            remote = RemoteClassDataSource(RetrofitClient.cartRemoteApiService),
            demo = DemoCartDataSource()
        )
    }
    val cartViewModel = remember { CartViewModel(cartRepository) }

    val cartState by cartViewModel.uiState.collectAsState()
    val cartCount = (cartState as? CartUiState.Success)?.cartCoffeeList?.size ?: 0

    NavHost(
        navController = navBarController,
        startDestination = NavBarRoutes.HomeScreen,
        enterTransition = {
            fadeIn(animationSpec = tween(400)) +
                scaleIn(initialScale = 0.92f, animationSpec = tween(400))
        },
        exitTransition = { fadeOut(animationSpec = tween(300)) },
        popEnterTransition = { fadeIn(animationSpec = tween(400)) },
        popExitTransition = { fadeOut(animationSpec = tween(400)) }
    ) {
        composable<NavBarRoutes.HomeScreen> {
            HomeScreen(
                navController = navBarController,
                viewModel = homeViewModel,
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
                cartCount = cartCount
            )
        }

        composable<NavBarRoutes.ProfileScreen> {
            ProfileScreen(
                navController = navBarController,
                cartCount = cartCount
            )
        }

        composable<NavBarRoutes.CoffeeDetailsScreen> { backStackEntry ->
            val route = backStackEntry.toRoute<NavBarRoutes.CoffeeDetailsScreen>()
            val homeState by homeViewModel.uiState.collectAsState()

            val selectedCoffee = (homeState as? HomeUiState.Success)
                ?.coffeeList
                ?.find { it.id == route.coffeeId }
                ?: coffeeItemList.find { it.id == route.coffeeId }
                ?: coffeeItemList[0]

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
    }
}
```

## 3) `HomeScreen.kt`
```kotlin
@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel,
    cartCount: Int,
    onAddToCartClick: (CoffeeItem) -> Unit
) {
    val categories = listOf(
        CoffeeCategory("1", "All Coffee"),
        CoffeeCategory("2", "Cappuccino"),
        CoffeeCategory("3", "Latte"),
        CoffeeCategory("4", "Americano"),
        CoffeeCategory("5", "Espresso"),
        CoffeeCategory("6", "Macchiato"),
        CoffeeCategory("7", "Mocha"),
        CoffeeCategory("8", "Flat White")
    )

    val selectedCategoryId by viewModel.selectedCategoryId.collectAsState()
    val selectedCategory = categories.find { it.id == selectedCategoryId } ?: categories[0]
    val searchQuery by viewModel.searchQuery.collectAsState()
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        bottomBar = { NavBarDesign(navController, "HomeScreen", cartCount) }
    ) { innerPadding ->
        // existing content unchanged
    }
}
```

## 4) `CartScreen.kt`
```kotlin
@Composable
fun CartScreen(
    onBackClick: () -> Unit,
    navController: NavHostController,
    viewModel: CartViewModel,
    cartCount: Int
) {
    val cartState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = Modifier.padding(top = 35.dp),
        containerColor = Color(0xFFF9F9F9),
        bottomBar = { NavBarDesign(navController, "CartScreen", cartCount) },
        topBar = {
            CartHeaderSection(
                onBackClick = onBackClick,
                onFavoriteClick = { }
            )
        }
    ) { innerPadding ->
        // existing content unchanged
    }
}
```

## 5) `NavBarDesign.kt`
```kotlin
@Composable
fun NavBarDesign(
    navController: NavHostController,
    currentRoute: String,
    cartCount: Int = 0
) {
    fun navigateToTab(route: Any) {
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }

    NavigationBar(containerColor = Color.White) {
        NavigationBarItem(
            selected = currentRoute == "HomeScreen",
            onClick = { navigateToTab(NavBarRoutes.HomeScreen) },
            icon = { Icon(painterResource(R.drawable.regular_outline_home), contentDescription = "Home") },
            label = { Text("Home") }
        )

        NavigationBarItem(
            selected = currentRoute == "CartScreen",
            onClick = { navigateToTab(NavBarRoutes.CartScreen) },
            icon = {
                BadgedBox(
                    badge = {
                        if (cartCount > 0) {
                            Badge { Text(cartCount.toString()) }
                        }
                    }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.regular_outline_bag),
                        contentDescription = "Cart"
                    )
                }
            },
            label = { Text("Cart") }
        )

        NavigationBarItem(
            selected = currentRoute == "HeartScreen",
            onClick = { navigateToTab(NavBarRoutes.HeartScreen) },
            icon = { Icon(painterResource(R.drawable.regular_outline_heart), contentDescription = "Heart") },
            label = { Text("Favorite") }
        )

        NavigationBarItem(
            selected = currentRoute == "ProfileScreen",
            onClick = { navigateToTab(NavBarRoutes.ProfileScreen) },
            icon = { Icon(painterResource(R.drawable.outline_account_circle_24), contentDescription = "Profile") },
            label = { Text("Profile") }
        )
    }
}
```

## 6) What to remove
- Delete the local `CartViewModel` creation inside the `CartScreen` destination in `NavBarGraph`.
- Do not keep a separate `CartViewModel()` inside `CartScreen` with `viewModel()`.
- Do not compute badge count inside `NavBarDesign` itself.

## 7) Result
- Badge updates after `addToCart()` from Home or Details.
- Badge stays visible across all bottom-bar screens.
- One cart state source prevents divergence.
- Adding more screens later does not require rewriting badge logic.
