package com.example.mycoffeeapp.ui.screens.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mycoffeeapp.ui.theme.CafeTextDark
import com.example.mycoffeeapp.ui.theme.CafeTextGray

@Composable
fun HelpCenterSheetContent() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Help Center",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = CafeTextDark
        )

        HelpCard(
            title = "Order issues",
            body = "If your order is delayed or incorrect, open the Orders tab and note the order ID."
        )
        HelpCard(
            title = "Payment",
            body = "Make sure the selected payment method is active before placing the order."
        )
        HelpCard(
            title = "Account support",
            body = "Use the account section to verify your email, phone number, and profile image."
        )
    }
}

@Composable
private fun HelpCard(title: String, body: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5EFE8)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, fontWeight = FontWeight.Bold, color = CafeTextDark)
            Spacer(modifier = Modifier.height(6.dp))
            Text(body, color = CafeTextGray, fontSize = 14.sp)
        }
    }
}
