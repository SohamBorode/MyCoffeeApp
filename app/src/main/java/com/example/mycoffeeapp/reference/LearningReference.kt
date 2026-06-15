package com.example.mycoffeeapp.reference

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.BlendMode

/*
From HomeScreen.kt:
------------------
@Composable
fun HomeScreen(navController: NavHostController) {
//    val listState = rememberLazyListState()

    val categories = listOf(
        CoffeeCategory("1", "All Coffee"),
        CoffeeCategory("2", "Machination"),
        CoffeeCategory("3", "Latte"),
        CoffeeCategory("4", "Cappuccino"),
        CoffeeCategory("5", "Espresso"),
        CoffeeCategory("6", "Americano"),
        CoffeeCategory("7", "Flat White"),
        CoffeeCategory("8", "Mocha"),
        CoffeeCategory("9", "Cold Brew")
    )
    val selectedCategory = remember { mutableStateOf(categories[0]) }


    Scaffold(
        bottomBar = { NavBarDesign(navController, "HomeScreen") }
    )
    { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(1f / 3f)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF303030),
                            Color(0xFF1F1F1F),
                            Color(0xFF121212)
                        )
                    )
                )
        )

        LazyColumnHS(

        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                "location",
                color = Color.LightGray,
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Arjun Nagar",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                )
                IconButton(
                    onClick = {}
                ) {
                    Icon(
                        painter = painterResource(R.drawable.regular_outline_arrow_down),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            SearchBar()
            Spacer(modifier = Modifier.height(15.dp))

            ReusableFilterItem(
                filters = categories,
                selectedFilter = selectedCategory.value,
                onFilterSelected = { selectedCategory.value = it },
   /*             modifier = Modifier
                    .graphicsLayer(compositingStrategy = androidx.compose.ui.graphics.CompositingStrategy.Offscreen)
                    .drawWithContent {
                        drawContent()

                        // Left shadow: Show if we have scrolled away from the start
                        val showLeftFade =
                            listState.firstVisibleItemIndex > 0 || listState.firstVisibleItemScrollOffset > 0
                        if (showLeftFade) {
                            drawRect(
                                brush = Brush.horizontalGradient(
                                    listOf(Color.Transparent, Color.Black),
                                    startX = 0f,
                                    endX = 50f // Width of the shadow
                                ),
                                blendMode = BlendMode.DstIn
                            )
                        }

                        val showRightFade = listState.canScrollForward
                        if (showRightFade) {
                            drawRect(
                                brush = Brush.horizontalGradient(
                                    listOf(Color.Black, Color.Transparent),
                                    startX = size.width - 50f,
                                    endX = size.width
                                ),
                                blendMode = BlendMode.DstIn
                            )
                        }
                    }*/
            )

            Spacer(modifier = Modifier.height(15.dp))
            LazyColumnHS()


        }
    }
}
*/

/*
From HomeScreen.kt (fading edges extension):
-------------------------------------------
fun Modifier.horizontalFadingEdges(
    scrollState: LazyListState,
    edgeWidth: Dp = 16.dp
): Modifier = this.then(
    Modifier
        .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
        .drawWithContent {
            drawContent()

            val edgeWidthPx = edgeWidth.toPx()

            // Left shadow logic
            if (scrollState.firstVisibleItemIndex > 0 || scrollState.firstVisibleItemScrollOffset > 0) {
                drawRect(
                    brush = Brush.horizontalGradient(
                        colors = listOf(Color.Transparent, Color.Black),
                        startX = 0f,
                        endX = edgeWidthPx
                    ),
                    blendMode = BlendMode.DstIn
                )
            }

            // Right shadow logic
            if (scrollState.canScrollForward) {
                drawRect(
                    brush = Brush.horizontalGradient(
                        colors = listOf(Color.Black, Color.Transparent),
                        startX = size.width - edgeWidthPx,
                        endX = size.width
                    ),
                    blendMode = BlendMode.DstIn
                )
            }
        }
)
*/

/*
From LasyColumnHS.kt (ItemCard and imports):
--------------------------------------------
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

/*
From LasyColumnHS.kt (LazyColumnHS alternative):
------------------------------------------------
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
*/