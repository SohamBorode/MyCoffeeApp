package com.example.mycoffeeapp.ui.screens.favorite

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.mycoffeeapp.ui.components.CoffeeCard
import com.example.mycoffeeapp.ui.navigation.NavBarDesign
import com.example.mycoffeeapp.ui.navigation.NavBarRoutes
import com.example.mycoffeeapp.ui.screens.home.HomeUiState
import com.example.mycoffeeapp.ui.screens.home.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeartScreen(
    navController: NavHostController,
    viewModel: FavoriteViewModel,
    onAddToCartClick: (com.example.mycoffeeapp.data.model.CoffeeItem) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Favorites",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF0C0F14)
                )
            )
        },
        bottomBar = { NavBarDesign(navController, "HeartScreen") },
        containerColor = Color(0xFF0C0F14)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val state = uiState) {
                is FavoriteUiState.Success -> {
                    val favorites = state.favoriteCoffeeList

                    if (favorites.isEmpty()) {
                        EmptyFavorites()
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            val rows = favorites.chunked(2)
                            items(rows) { rowItems ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    for (item in rowItems) {
                                        CoffeeCard(
                                            item = item,
                                            onAddClick = { onAddToCartClick(item) },
                                            onCardClick = {
                                                navController.navigate(
                                                    NavBarRoutes.CoffeeDetailsScreen(coffeeId = item.id)
                                                )
                                            },
                                            onFavoriteClick = { viewModel.toggleFavorite(item.id) },
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                    if (rowItems.size == 1) Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }
                is FavoriteUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is FavoriteUiState.Error -> {
                    Text(
                        text = state.msg,
                        color = Color.Red,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is FavoriteUiState.Empty -> {
                    EmptyFavorites()
                }
            }
        }
    }
}

@Composable
fun EmptyFavorites() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Favorite,
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.size(100.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "No Favorites Yet",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            "Start adding your favorite coffee!",
            color = Color.Gray,
            fontSize = 14.sp
        )
    }
}
