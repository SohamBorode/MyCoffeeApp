package com.example.mycoffeeapp.ui.screens.favorite

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.mycoffeeapp.ui.navigation.NavBarDesign

@Composable
fun HeartScreen(navController: NavHostController) {
    Scaffold(
        bottomBar = { NavBarDesign(navController, "HeartScreen") }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Text("Favorite Screen")
        }
    }
}