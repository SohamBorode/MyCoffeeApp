package com.example.mycoffeeapp.ui.screens.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mycoffeeapp.ui.theme.CafeTextDark
import com.example.mycoffeeapp.ui.theme.CafeTextGray

@Composable
fun AccountSheetContent(state: AccountUiState) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 32.dp)
            .verticalScroll(rememberScrollState()) // Enables smooth expansion on scroll
    ) {
        Text(
            text = "My Account",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = CafeTextDark,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        when (state) {
            is AccountUiState.Loading -> {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(200.dp), contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = CafeTextDark)
                }
            }

            is AccountUiState.Success -> {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    AccountDetailItem("Username", state.username)
                    AccountDetailItem("Full Name", state.fullName)
                    AccountDetailItem("Email", state.email)
                    AccountDetailItem("Phone", state.phonNo)
                    AccountDetailItem("Date Of Birth", state.dob)

                }
            }

            is AccountUiState.Error -> Text(text = state.msg, color = Color.Red)
        }
    }
}

@Composable
fun AccountDetailItem(filed: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(
            text = "${filed}:",
            color = CafeTextGray,
            fontSize = 25.sp,
            fontWeight = FontWeight.Medium
        )
        Text(text = value,
            color = CafeTextGray,
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold)

    }
}