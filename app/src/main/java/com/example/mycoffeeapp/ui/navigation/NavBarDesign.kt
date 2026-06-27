package com.example.mycoffeeapp.ui.navigation

import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.mycoffeeapp.R

@Composable
fun NavBarDesign(
    navController: NavHostController,
    currentRoute: NavBarRoutes,
    cartCount: Int = 0
) {
    fun navigateToTab(route: NavBarRoutes) {
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }

    NavigationBar(containerColor = Color.White) {
        NavigationBarItem(
            selected = currentRoute == NavBarRoutes.HomeScreen,
            onClick = { navigateToTab(NavBarRoutes.HomeScreen) },
            icon = {
                Icon(
                    painter = painterResource(R.drawable.regular_outline_home),
                    contentDescription = "Home"
                )
            },
            label = { Text("Home") }
        )
        NavigationBarItem(
            selected = currentRoute == NavBarRoutes.CartScreen,
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
            selected = currentRoute == NavBarRoutes.HeartScreen,
            onClick = { navigateToTab(NavBarRoutes.HeartScreen) },
            icon = {
                Icon(
                    painter = painterResource(R.drawable.regular_outline_heart),
                    contentDescription = "Favorite"
                )
            },
            label = { Text("Favorite") }
        )
        NavigationBarItem(
            selected = currentRoute == NavBarRoutes.ProfileScreen,
            onClick = { navigateToTab(NavBarRoutes.ProfileScreen) },
            icon = {
                Icon(
                    painter = painterResource(R.drawable.outline_account_circle_24),
                    contentDescription = "Profile"
                )
            },
            label = { Text("Profile") }
        )
    }
}

/*
package com.example.mycoffeeapp.ui.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.mycoffeeapp.R


@Composable
fun NavBarDesign(
    navController: NavHostController,
    currentRoute: NavBarRoutes,
    cartCount: Int = 0
) {

    fun navigateToTab(route: NavBarRoutes) {
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }

    NavigationBar(
        containerColor = Color.White
    ) {
        NavigationBarItem(
            selected = currentRoute == NavBarRoutes.HomeScreen,
            onClick = { navigateToTab(NavBarRoutes.HomeScreen) },
            icon = {
                Icon(
                    painterResource(R.drawable.regular_outline_home),
                    contentDescription = "Home"
                )
            },
            label = { Text("Home") }
        )
        NavigationBarItem(
            selected = currentRoute == NavBarRoutes.CartScreen,
            onClick = { navigateToTab(NavBarRoutes.CartScreen) },
            icon = {
                BadgedBox(
                    badge = {
                        if (cartCount > 0) {
                            Badge {
                                Text(cartCount.toString())
                            }
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
            selected = currentRoute == NavBarRoutes.HeartScreen,
            onClick = { navigateToTab(NavBarRoutes.HeartScreen) },
            icon = {
                Icon(
                    painterResource(R.drawable.regular_outline_heart),
                    contentDescription = "Heart"
                )
            },
            label = { Text("Favorite") }
        )
        NavigationBarItem(
            selected = currentRoute == NavBarRoutes.ProfileScreen,
            onClick = { navigateToTab(NavBarRoutes.ProfileScreen) },
            icon = {
                Icon(
                    painterResource(R.drawable.outline_account_circle_24),
                    contentDescription = "Profile"
                )
            },
            label = { Text("Profile") }
        )
    }
}
*/