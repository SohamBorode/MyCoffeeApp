package com.example.mycoffeeapp.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.mycoffeeapp.R
import com.example.mycoffeeapp.data.model.CoffeeItem
import com.example.mycoffeeapp.ui.theme.CafeBrown
import com.example.mycoffeeapp.ui.theme.CafeCream
import com.example.mycoffeeapp.ui.theme.CafeTextDark
import com.example.mycoffeeapp.ui.theme.CafeTextGray

// Enum for Coffee Size with price multipliers
enum class CoffeeSize(val priceMultiplier: Double) {
    S(0.8),
    M(1.0),
    L(1.2)
}

// Enum for Coffee Temperature with price adjustments
enum class CoffeeTemperature(val priceAdjustment: Double) {
    Hot(0.0),
    Ice(0.5)
}

data class CoffeeDetailState(
    val coffeeItem: CoffeeItem,
    val selectedSize: CoffeeSize = CoffeeSize.valueOf(coffeeItem.size),
    val selectedTemperature: CoffeeTemperature = CoffeeTemperature.valueOf(coffeeItem.temperature),
    val quantity: Int = 1
) {
    // Logic to calculate price based on size and temperature
    fun calculatePrice(): Double {
        val basePrice = coffeeItem.price
        return (basePrice * selectedSize.priceMultiplier + selectedTemperature.priceAdjustment) * quantity
    }
}

@Composable
fun CoffeeDetailsScreen(
    initialState: CoffeeDetailState,
    onBackClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onAddToCartEndpoint: (String, CoffeeSize, CoffeeTemperature, Double) -> Unit
) {
    var state by remember { mutableStateOf(initialState) }

    Scaffold(
        containerColor = CafeCream, // Using theme color
        bottomBar = {
            BottomBuyBar(
                price = state.calculatePrice(),
                onAddToCartClick = {
                    onAddToCartEndpoint(
                        state.coffeeItem.id,
                        state.selectedSize,
                        state.selectedTemperature,
                        state.calculatePrice()
                    )
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            HeaderSection(
                onBackClick = onBackClick,
                onFavoriteClick = onFavoriteClick,
                isFavorite = state.coffeeItem.isFavorite
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Large Coffee Image
            AsyncImage(
                model = state.coffeeItem.imageUrl,
                contentDescription = state.coffeeItem.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Title and Temperature Selection
            Text(
                text = state.coffeeItem.name,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = CafeTextDark
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Ice",
                        fontSize = 14.sp,
                        color = if (state.selectedTemperature == CoffeeTemperature.Ice) CafeBrown else CafeTextGray,
                        modifier = Modifier.clickable { state = state.copy(selectedTemperature = CoffeeTemperature.Ice) }
                    )
                    Text(" / ", fontSize = 14.sp, color = CafeTextGray)
                    Text(
                        text = "Hot",
                        fontSize = 14.sp,
                        color = if (state.selectedTemperature == CoffeeTemperature.Hot) CafeBrown else CafeTextGray,
                        modifier = Modifier.clickable { state = state.copy(selectedTemperature = CoffeeTemperature.Hot) }
                    )
                }
                // Coffee bean icon
                Icon(
                    painter = painterResource(id = R.drawable.default_bean),
                    contentDescription = null,
                    tint = CafeBrown,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = Color(0xFFEAEAEA), thickness = 1.dp)
            Spacer(modifier = Modifier.height(16.dp))

            // Description Section
            Text(
                text = "Description",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = CafeTextDark
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = state.coffeeItem.description,
                fontSize = 14.sp,
                color = CafeTextGray,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Size Selection Section
            Text(
                text = "Size",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = CafeTextDark
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CoffeeSize.entries.forEach { size ->
                    SizeOption(
                        size = size.name,
                        isSelected = state.selectedSize == size,
                        onClick = { state = state.copy(selectedSize = size) }
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun HeaderSection(onBackClick: () -> Unit, onFavoriteClick: () -> Unit, isFavorite: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                painter = painterResource(id = R.drawable.regular_outline_arrow_left),
                contentDescription = "Back",
                modifier = Modifier.size(24.dp)
            )
        }
        Text("Detail", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = CafeTextDark)
        IconButton(onClick = onFavoriteClick) {
            Icon(
                imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = "Favorite",
                tint = if (isFavorite) Color.Red else CafeTextDark
            )
        }
    }
}

@Composable
fun SizeOption(size: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .width(100.dp)
            .height(45.dp)
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = 1.dp,
                color = if (isSelected) CafeBrown else Color(0xFFEAEAEA),
                shape = RoundedCornerShape(12.dp)
            )
            .background(if (isSelected) CafeCream else Color.White)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = size,
            color = if (isSelected) CafeBrown else CafeTextDark,
            fontSize = 16.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
fun BottomBuyBar(price: Double, onAddToCartClick: () -> Unit) {
    Surface(
        shadowElevation = 16.dp,
        modifier = Modifier.fillMaxWidth(),
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Price", color = CafeTextGray, fontSize = 14.sp)
                Text(
                    text = "$ ${String.format("%.2f", price)}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = CafeBrown
                )
            }
            Button(
                onClick = onAddToCartClick,
                modifier = Modifier
                    .height(56.dp)
                    .width(200.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CafeBrown)
            ) {
                Text("Add To Cart", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}