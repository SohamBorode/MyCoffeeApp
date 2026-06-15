package com.example.mycoffeeapp.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mycoffeeapp.data.model.CoffeeItem
import com.example.mycoffeeapp.ui.navigation.NavBarRoutes


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LazyColumnHS(
    navController: NavHostController,
    coffeeItemList: List<CoffeeItem>,
    header: @Composable () -> Unit = {},
    stickyHeader: @Composable () -> Unit = {},
    banner: @Composable () -> Unit = {}
) {
    val listState = rememberLazyListState()
    val isStuck by remember {
        derivedStateOf { listState.firstVisibleItemIndex > 0 }
    }
    
    val infiniteTransition = rememberInfiniteTransition(label = "backgroundPulse")
    val animatedAlpha by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Pulse"
    )

    val stickyBrush = if (isStuck) {
        Brush.verticalGradient(
            colors = listOf(
                Color(0xFF121212),
                Color(0xFF121212),
                Color(0xFF121212),
                Color(0xFF121212),
                Color(0xFF121212),
                Color(0xFF121212),
                Color(0xFF252525).copy(alpha = animatedAlpha),
                Color(0xFF252525).copy(alpha = animatedAlpha)
            )
        )
    } else {
        Brush.verticalGradient(listOf(Color.Transparent, Color.Transparent))
    }

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        item { header() }
        stickyHeader {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(stickyBrush)
                    .padding(vertical = 4.dp)
            ) {
                stickyHeader()
            }
        }
        item { banner() }
        
        val rows = coffeeItemList.chunked(2)
        items(rows) { rowItems ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                for (item in rowItems) {
                    CoffeeCard(
                        item = item,
                        onAddClick = {},
                        onCardClick = {
                            navController.navigate(
                                NavBarRoutes.CoffeeDetailsScreen(
                                    coffeeId = item.id
                                )
                            )
                        },
                        onFavoriteClick = {},
                        modifier = Modifier.weight(1f)
                    )
                }
                if (rowItems.size == 1) Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}