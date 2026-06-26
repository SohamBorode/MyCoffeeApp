package com.example.mycoffeeapp.ui.screens.loginsignp

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.mycoffeeapp.data.local.SessionManager
import com.example.mycoffeeapp.ui.navigation.NavRoutes
import com.example.mycoffeeapp.ui.theme.CafeBrown
import com.example.mycoffeeapp.ui.theme.CafeCream
import com.example.mycoffeeapp.ui.theme.CafeTextDark
import com.example.mycoffeeapp.ui.theme.DarkMocha
import com.example.mycoffeeapp.ui.theme.LightGray
import com.example.mycoffeeapp.ui.theme.OffWhite
import com.example.mycoffeeapp.ui.theme.PureWhite

@Composable
//@Preview(showSystemUi = true)
fun LoginScreen(navControllerX: NavHostController, authViewModel: AuthViewModel) {
    // State for the email input field
    var email by rememberSaveable { mutableStateOf("") }
    var username by rememberSaveable { mutableStateOf("") }
    // State for the password input field
    var password by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val state by authViewModel.uiState.collectAsState()

    // Reset state when entering to prevent stale states from previous screens
    LaunchedEffect(Unit) {
        authViewModel.loadAuthpage()
    }

    LaunchedEffect(state) {
        when (state) {
            is AuthUiState.Success -> {
                navControllerX.navigate(NavRoutes.NavBarGraph) {
                    // Clear the entire auth flow backstack
                    popUpTo(NavRoutes.WelcomeScreen) { inclusive = true }
                }
            }

            is AuthUiState.Error -> {
                Toast.makeText(context, (state as AuthUiState.Error).msg, Toast.LENGTH_SHORT).show()
            }

            else -> {}
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = CafeCream),
        contentAlignment = Alignment.Center
    ) {
        if (state is AuthUiState.Loading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = CafeTextDark
            )
        }
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Login Page", fontSize = 32.sp)


            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(text = "Email/Username") }
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(text = "Password") }
            )
            Spacer(modifier = Modifier.height(16.dp))
            // Login Button
            Button(
                onClick = {
                    authViewModel.login(email, password)

                },

                // Logic Fix: Disable the button while loading to prevent multiple requests
                enabled = state !is AuthUiState.Loading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = CafeBrown,
                    disabledContainerColor = LightGray,
                    contentColor = DarkMocha
                )
            ) {
                Text(text = if (state is AuthUiState.Loading) "Logging in..." else "Login")
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Navigation to Signup Page
            TextButton(onClick = {
                val sharedPref = context.getSharedPreferences("UserInfo", Context.MODE_PRIVATE)
                navControllerX.navigate(NavRoutes.SignupScreen) {
                    // Pop Login so the stack is [Welcome, Signup]
                    popUpTo(NavRoutes.LoginScreen) { inclusive = true }
                }
            }
            ) {
                Text(text = "Don't have an account! Signup", color = CafeBrown)
            }
        }
    }

}