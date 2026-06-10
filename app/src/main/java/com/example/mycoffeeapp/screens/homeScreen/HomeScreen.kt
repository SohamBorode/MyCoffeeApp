package com.example.mycoffeeapp.screens.homeScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.mycoffeeapp.R
import com.example.mycoffeeapp.navigation.navbar.NavBarDesign
import com.example.mycoffeeapp.screens.uipeices.LazyColumnHS
import com.example.mycoffeeapp.screens.uipeices.SearchBar
import com.example.mycoffeeapp.screens.uipeices.filtertab.CoffeeCategory
import com.example.mycoffeeapp.screens.uipeices.filtertab.ReusableFilterItem

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
                    "nsdjnksnvnkjnv NAgar",
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
/*
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