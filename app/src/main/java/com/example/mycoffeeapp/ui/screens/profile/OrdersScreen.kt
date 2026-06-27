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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mycoffeeapp.data.model.dto.OrderDto
import com.example.mycoffeeapp.ui.theme.CafeTextDark
import com.example.mycoffeeapp.ui.theme.CafeTextGray
import java.text.NumberFormat
import java.util.Locale

@Composable
fun OrdersSheetContent(state: OrdersUiState) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "My Orders",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = CafeTextDark,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        when (state) {
            is OrdersUiState.Loading -> CircularProgressIndicator(color = CafeTextDark)
            is OrdersUiState.Error -> Text(text = state.msg, color = Color.Red)
            is OrdersUiState.Success -> {
                if (state.orders.isEmpty()) {
                    Text(text = "No orders yet.", color = CafeTextGray)
                } else {
                    state.orders.forEach { order ->
                        OrderCard(order)
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun OrderCard(order: OrderDto) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5EFE8)),
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text("Order #${order.orderId}", fontWeight = FontWeight.Bold, color = CafeTextDark)
            Text(order.date, color = CafeTextGray)
            Text(order.status, color = CafeTextGray)
            HorizontalDivider()
            Text(
                text = money(order.totalAmount),
                color = CafeTextDark,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

private fun money(value: Double): String {
    return NumberFormat.getCurrencyInstance(Locale("en", "IN")).format(value)
}
