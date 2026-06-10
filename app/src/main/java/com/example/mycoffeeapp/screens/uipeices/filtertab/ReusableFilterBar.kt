package com.example.mycoffeeapp.screens.uipeices.filtertab

import android.widget.Filter
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDecay
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.size
import androidx.compose.ui.unit.width
import com.example.mycoffeeapp.ui.theme.CoffeeBrown
import com.example.mycoffeeapp.ui.theme.Latte
import com.example.mycoffeeapp.ui.theme.LightGray

@Composable
fun <T : FilterItem> ReusableFilterItem(
    modifier: Modifier = Modifier,
    contentPaddingValues: PaddingValues = PaddingValues(16.dp, 8.dp),
    selectedFilter: T?,
    onFilterSelected: (T) -> Unit,
    filters: List<T>
) {

    val listState = rememberLazyListState()

    LazyRow(
        state = listState,
        contentPadding = contentPaddingValues,
        modifier = modifier.fadingEdges(listState),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(filters, key = { it.id }) { filter ->
            val isSelected = (filter.id == selectedFilter?.id)

            val backgroundColor by animateColorAsState(
                targetValue = if (isSelected) {
                    Latte
                } else {
                    LightGray
                },
                label = "BackgroundAnimation"
            )

            val contentColor by animateColorAsState(
                targetValue = if (isSelected) {
                    MaterialTheme.colorScheme.onPrimary
                } else {
                    MaterialTheme.colorScheme.onSecondary
                },
                label = "ContentAnimation"
            )

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(15.dp))
                    .background(backgroundColor)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        onFilterSelected(filter)
                    }
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = filter.filterName,
                    color = contentColor,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
            }


        }

    }
}

fun Modifier.fadingEdges(
    scrollState: LazyListState,
    edgeWidth: Dp = 20.dp
): Modifier = this.then(
    Modifier
        .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
        .drawWithContent {
            drawContent()

            val edgeWidthPx = edgeWidth.toPx()

            // Left Shadow: Only visible when scrolled away from start
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

            // Right Shadow: Only visible if more items are ahead
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