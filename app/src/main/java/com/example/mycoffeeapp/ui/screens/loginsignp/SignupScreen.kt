package com.example.mycoffeeapp.ui.screens.loginsignp

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.mycoffeeapp.ui.navigation.NavRoutes

@Composable
fun SignupScreen(navControllerX: NavHostController, authViewModel: com.example.mycoffeeapp.ui.screens.loginsignp.AuthViewModel) {

    // State management for user input
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var name by rememberSaveable { mutableStateOf("") }
    var username by rememberSaveable { mutableStateOf("") }

    // Observes the ViewModel's state
    val state by authViewModel.uiState.collectAsState()
    val context = LocalContext.current

    // Reset state when entering to prevent stale states from previous screens
    LaunchedEffect(Unit) {
        authViewModel.loadAuthpage()
    }

    // Handles side-effects based on state changes
    LaunchedEffect(state) {
        when (state) {
            is com.example.mycoffeeapp.ui.screens.loginsignp.AuthUiState.Success -> {
                navControllerX.navigate(NavRoutes.NavBarGraph) {
                    popUpTo(NavRoutes.WelcomeScreen) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            }

            is com.example.mycoffeeapp.ui.screens.loginsignp.AuthUiState.Error -> Toast.makeText(
                context,
                (state as com.example.mycoffeeapp.ui.screens.loginsignp.AuthUiState.Error).msg,
                Toast.LENGTH_SHORT
            ).show()

            else -> {}
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Signup Page", fontSize = 32.sp)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text(text = "Full name") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text(text = "Username") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(text = "Email") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(text = "Password") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Signup Button
        Button(
            onClick = {
                authViewModel.signup(
                    name = name,
                    email = email,
                    password = password,
                    username = username,
                )
            },
            //  Button is enabled as long as we are NOT loading
            enabled = state != _root_ide_package_.com.example.mycoffeeapp.ui.screens.loginsignp.AuthUiState.Loading
        ) {
            Text(text = if (state is com.example.mycoffeeapp.ui.screens.loginsignp.AuthUiState.Loading) "Creating Account..." else "Create Account")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Navigation to Login Page
        TextButton(onClick = { 
            navControllerX.navigate(NavRoutes.LoginScreen) {
                popUpTo(NavRoutes.SignupScreen) { inclusive = true }
            }
        }) {
            Text(text = "Have an account, Login")
        }
    }
}
