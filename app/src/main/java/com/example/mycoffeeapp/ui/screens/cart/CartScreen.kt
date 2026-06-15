package com.example.mycoffeeapp.ui.screens.cart

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.mycoffeeapp.R
import com.example.mycoffeeapp.data.model.cart.CartItem
import com.example.mycoffeeapp.ui.navigation.NavBarDesign
import com.example.mycoffeeapp.ui.theme.CafeBrown
import com.example.mycoffeeapp.ui.theme.CafeTextDark
import com.example.mycoffeeapp.ui.theme.CafeTextGray
import com.example.mycoffeeapp.ui.theme.CreamyCoffee

@Composable
fun CartScreen(
    onBackClick: () -> Unit,
    navController: NavHostController,
    viewModel: CartViewModel = viewModel()
) {

    val cartState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = Modifier.padding(top = 35.dp),
        containerColor = Color(0xFFF9F9F9), // Soft light background
        bottomBar = { NavBarDesign(navController, "CartScreen") },
        topBar = {
            CartHeaderSection(
                onBackClick = onBackClick,
                onFavoriteClick = {/*Open the favourite screen*/ }
            )
        }

    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            when (val state = cartState) {
                is CartUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                is CartUiState.Success -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(bottom = 16.dp) // Extra spacing at bottom
                    ) {
                        items(items = state.cartCoffeeList, key = { it.id }) { cartCoffeeItem ->
                            CartCoffeeItem(
                                item = cartCoffeeItem,
                                onDeleteClick = { viewModel.deleteItem(cartCoffeeItem.id) })
                        }
                    }
                }

                is CartUiState.Error -> {
                    Text(text = state.msg, modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    }
}

@Composable
fun CartHeaderSection(onBackClick: () -> Unit, onFavoriteClick: () -> Unit) {
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
        Text("My Cart", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = CafeTextDark)
        IconButton(onClick = onFavoriteClick) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "Favorite",
                tint = CafeTextDark
            )
        }
    }
}


@Composable
fun CartCoffeeItem(
    item: CartItem,
    onDeleteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(Color.White, RoundedCornerShape(16.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Product Image
        AsyncImage(
            model = if (item.imageUrl.isEmpty()) R.drawable.coffee_5 else item.imageUrl,
            contentDescription = item.name,
            modifier = Modifier
                .size(65.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(12.dp))

        // Product Details
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = item.name,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = CafeBrown
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                DetailItem(label = "Price", value = item.price.toString())
                DetailItem(label = "Size", value = item.size)
                DetailItem(label = "Temp", value = item.temperature)
            }
        }

        // Action Buttons
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.End
        ) {
            Button(
                onClick = { /* Handle View */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = CreamyCoffee,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.height(32.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp)
            ) {
                Text("View", fontSize = 12.sp)
            }

            Button(
                onClick = { onDeleteClick() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = CafeBrown
                ),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, CafeBrown),
                modifier = Modifier.height(32.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp)
            ) {
                Text("Delete", fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun DetailItem(label: String, value: String) {
    Column {
        Text(text = label, color = CafeTextGray, fontSize = 11.sp)
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = CafeBrown
        )
    }
}

