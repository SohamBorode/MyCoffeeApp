package com.example.mycoffeeapp.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.mycoffeeapp.R
import com.example.mycoffeeapp.data.model.CoffeeCategory
import com.example.mycoffeeapp.ui.components.LazyColumnHS
import com.example.mycoffeeapp.ui.components.SearchBar
import com.example.mycoffeeapp.ui.components.ReusableFilterBar
import com.example.mycoffeeapp.ui.components.fadingEdges
import com.example.mycoffeeapp.ui.navigation.NavBarDesign
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(navController: NavHostController, viewModel: HomeViewModel) {
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
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        bottomBar = { NavBarDesign(navController, "HomeScreen") }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(1f / 3f)
                    .background(Brush.linearGradient(listOf(Color(0xFF303030), Color(0xFF121212))))
            )

            when (val uiState = state) {
                is HomeUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color(0xFFC49A6C))
                    }
                }
                is HomeUiState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = uiState.msg, color = Color.Red)
                            Button(onClick = { viewModel.loadCoffeeData() }) {
                                Text("Retry")
                            }
                        }
                    }
                }
                is HomeUiState.Success -> {
                    LazyColumnHS(
                        navController = navController,
                        coffeeItemList = uiState.coffeeList,
                        header = {
                            LocationHeader()
                        },
                        stickyHeader = {
                            Column(modifier = Modifier.padding(bottom = 8.dp)) {
                                SearchBar()
                                ReusableFilterBar(
                                    filters = categories,
                                    selectedFilter = selectedCategory.value,
                                    onFilterSelected = { selectedCategory.value = it }
                                )
                            }
                        },
                        banner = {
                            BannerSection()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun BannerSection() {
    val bannerList = listOf(
        R.drawable.banner_1,
        R.drawable.banner_1,
        R.drawable.banner_1,
        R.drawable.banner_1,
        R.drawable.banner_1
    )
    val bannerListState = rememberLazyListState()

    LaunchedEffect(key1 = bannerListState) {
        var currIdx = 0
        while (true) {
            delay(2000L)
            if (!bannerListState.isScrollInProgress) {
                if (currIdx >= bannerList.size - 1) {
                    currIdx = 0
                    bannerListState.scrollToItem(0)
                } else {
                    currIdx++
                    bannerListState.animateScrollToItem(currIdx)
                }
            }
        }
    }
    LazyRow(
        state = bannerListState,
        modifier = Modifier
            .fillMaxWidth()
            .fadingEdges(bannerListState)
    ) {
        items(bannerList) { banner ->
            Image(
                painter = painterResource(banner),
                contentDescription = null,
                modifier = Modifier
                    .padding(16.dp)
                    .width(320.dp)
                    .height(160.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )
        }
        item { Spacer(modifier = Modifier.width(15.dp)) }
    }
}

@Composable
fun LocationHeader() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Location", color = Color.LightGray, fontSize = 12.sp)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "Bilaspur, India",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Icon(
                painter = painterResource(R.drawable.regular_outline_arrow_down),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .padding(start = 4.dp)
                    .size(16.dp)
            )
        }
    }
}