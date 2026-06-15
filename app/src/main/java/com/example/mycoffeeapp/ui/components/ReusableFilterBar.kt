package com.example.mycoffeeapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mycoffeeapp.data.model.CoffeeCategory

@Composable
fun ReusableFilterBar(
    filters: List<CoffeeCategory>,
    selectedFilter: CoffeeCategory,
    onFilterSelected: (CoffeeCategory) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        modifier = Modifier.height(40.dp)
    ) {
        items(filters) { category ->
            val isSelected = category.id == selectedFilter.id
            Box(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (isSelected) Color(0xFFC49A6C) else Color(0xFF202020))
                    .clickable { onFilterSelected(category) }
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = category.name,
                    color = if (isSelected) Color.White else Color.Gray,
                    fontSize = 14.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}

fun Modifier.fadingEdges(scrollState: LazyListState): Modifier = this.then(
    Modifier
        .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
        .drawWithContent {
            drawContent()
            if (scrollState.firstVisibleItemIndex > 0 || scrollState.firstVisibleItemScrollOffset > 0) {
                drawRect(
                    brush = Brush.horizontalGradient(
                        listOf(Color.Transparent, Color.Black),
                        startX = 0f,
                        endX = 50f
                    ),
                    blendMode = BlendMode.DstIn
                )
            }
            if (scrollState.canScrollForward) {
                drawRect(
                    brush = Brush.horizontalGradient(
                        listOf(Color.Black, Color.Transparent),
                        startX = size.width - 50f,
                        endX = size.width
                    ),
                    blendMode = BlendMode.DstIn
                )
            }
        }
)