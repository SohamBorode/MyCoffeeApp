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
fun TermsSheetContent() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Terms and Conditions",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = CafeTextDark
        )

        TermsCard(
            title = "1. Use of the app",
            body = "Use this app only for lawful ordering and account management."
        )
        TermsCard(
            title = "2. Orders",
            body = "Orders are confirmed only after the final confirmation step."
        )
        TermsCard(
            title = "3. Account data",
            body = "You are responsible for keeping your profile information accurate."
        )
        TermsCard(
            title = "4. Privacy",
            body = "Profile and order data are used to personalize your experience."
        )
    }
}

@Composable
private fun TermsCard(title: String, body: String) {
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
