package com.example.mycoffeeapp.screens.heartscreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.mycoffeeapp.navigation.navbar.NavBarDesign


@Composable
fun HeartScreen(navController: NavHostController){
    Scaffold(
        bottomBar = { NavBarDesign(navController, "HeartScreen") }
    ) {innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding)
        )
    }
}