package com.example.mycoffeeapp.screens.uipeices
/*
import android.icu.text.CaseMap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun ItemCard(cardEle: CardEle) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .padding(8.dp)
                .height(150.dp)
                .align(alignment = Alignment.Start),
        ) {
            Image(
                painter = cardEle.img,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(3f / 5f),
                
            )
        }
    }
}
*/

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mycoffeeapp.R


@Composable
fun LazyColumnHS() {

    val coffeeItemList = listOf(
        CoffeeItem(
            id = "1",
            name = "Cappuccino",
            description = "With Chocolate",
            price = 250.0,
            imageRes = R.drawable.coffee_1,
            isFavorite = true
        ),
        CoffeeItem(
            id = "2",
            name = "Espresso",
            description = "Strong & Bold",
            price = 180.0,
            imageRes = R.drawable.coffee_2
        ),
        CoffeeItem(
            id = "3",
            name = "Latte",
            description = "Rich & Creamy",
            price = 220.0,
            imageRes = R.drawable.coffee_3
        ),
        CoffeeItem(
            id = "4",
            name = "Mocha",
            description = "Chocolate flavor",
            price = 280.0,
            imageRes = R.drawable.coffee_4
        ),
        CoffeeItem(
            id = "5",
            name = "Flat White",
            description = "Smooth & Silky",
            price = 240.0,
            imageRes = R.drawable.coffee_5
        ),
        CoffeeItem(
            id = "6",
            name = "Americano",
            description = "Classic Black",
            price = 150.0,
            imageRes = R.drawable.coffee_6
        ),
        CoffeeItem(
            id = "7",
            name = "Macchiato",
            description = "Caramel Drizzle",
            price = 300.0,
            imageRes = R.drawable.coffee_1 // Reusing an icon for example
        )
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // 1. item 
        item (span = { GridItemSpan(2) }){
            Image(
                painter = painterResource(R.drawable.banner_1),
                contentDescription = "Home Screen Banner",
                modifier = Modifier
                    .height(120.dp)
                    .fillMaxWidth(2f / 3f)
                    .clip(shape = RoundedCornerShape(16.dp)),
//                contentScale = ContentScale.Crop,

            )
        }
// 2. items
        items(coffeeItemList) { item ->
            CoffeeCard(
                item = item,
                onAddClick = {},
                onFavoriteClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
        }

    }
}

data class CoffeeItem(
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val currencySymbol: String = "₹",
    val imageRes: Int,
    val isFavorite: Boolean = false
)

@Composable
fun CoffeeCard(
    item: CoffeeItem,
    onAddClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(), // Fits a 2-column grid layout
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Image Section with Favorite Button Overlay
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f) // Ensures a square aspect ratio
            ) {
                Image(
                    // Replace with AsyncImage (Coil library) for production network images
                    painter = painterResource(id = item.imageRes),
                    contentDescription = item.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                )

                // Favorite Button Overlay
                IconButton(
                    onClick = onFavoriteClick,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(32.dp)
                        .background(
                            color = Color.White.copy(alpha = 0.6f),
                            shape = RoundedCornerShape(8.dp)
                        )
                ) {
                    Icon(
                        imageVector = if (item.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (item.isFavorite) Color.Red else Color.Gray,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Text Info Section
            Text(
                text = item.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333)
            )

            Text(
                text = item.description,
                fontSize = 12.sp,
                color = Color.Gray,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Bottom Row: Price and Add Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${item.currencySymbol}${item.price}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF8B4513), // Coffee brown brand accent
                    modifier = Modifier.weight(1f)
                )

                IconButton(
                    onClick = onAddClick,
                    modifier = Modifier
                        .size(36.dp)
                        .background(
                            color = Color(0xFFC49A6C), // Soft brown add button background
                            shape = RoundedCornerShape(10.dp)
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add to cart",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

