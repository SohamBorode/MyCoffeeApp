package com.example.mycoffeeapp.ui.screens.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.example.mycoffeeapp.R
import com.example.mycoffeeapp.data.model.CoffeeCategory
import com.example.mycoffeeapp.data.model.CoffeeItem
import com.example.mycoffeeapp.ui.components.LazyColumnHS
import com.example.mycoffeeapp.ui.components.ReusableFilterBar
import com.example.mycoffeeapp.ui.components.SearchBar
import com.example.mycoffeeapp.ui.components.fadingEdges
import com.example.mycoffeeapp.ui.navigation.NavBarDesign
import com.example.mycoffeeapp.ui.screens.favorite.FavoriteViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.delay
import java.util.Locale

@Composable
fun HomeScreen(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    favoriteViewModel : FavoriteViewModel,
    cartCount : Int,
    onAddToCartClick : (CoffeeItem) -> Unit
) {
    val categories = listOf(
        CoffeeCategory("1", "All Coffee"),
        CoffeeCategory("2", "Cappuccino"),
        CoffeeCategory("3", "Latte"),
        CoffeeCategory("4", "Americano"),
        CoffeeCategory("5", "Espresso"),
        CoffeeCategory("6", "Macchiato"),
        CoffeeCategory("7", "Mocha"),
        CoffeeCategory("8", "Flat White")
    )
    val selectedCategoryId by homeViewModel.selectedCategoryId.collectAsState()
    val selectedCategory = categories.find { it.id == selectedCategoryId } ?: categories[0]
    val searchQuery by homeViewModel.searchQuery.collectAsState()
    val state by homeViewModel.uiState.collectAsState()

    Scaffold(
        bottomBar = { NavBarDesign(navController, "HomeScreen", cartCount) }
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
                            Button(onClick = { homeViewModel.loadCoffeeData() }) {
                                Text("Retry")
                            }
                        }
                    }
                }
                is HomeUiState.Success -> {
                    LazyColumnHS(
                        navController = navController,
                        coffeeItemList = uiState.coffeeList,
                        onFavoriteClick = { favoriteViewModel.toggleFavorite(it) },
                        onAddClick = {onAddToCartClick(it)},
                        header = {
                            LocationHeader()
                        },
                        stickyHeader = {
                            Column(modifier = Modifier.padding(bottom = 8.dp)) {
                                SearchBar(
                                    value = searchQuery,
                                    onValueChange = { homeViewModel.onSearchQueryChange(it) },
                                    onSearchClick = { homeViewModel.onSearchQueryChange(it) }
                                )
                                ReusableFilterBar(
                                    filters = categories,
                                    selectedFilter = selectedCategory,
                                    onFilterSelected = { homeViewModel.filterByItem(it.id) }
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


/*
@Composable
fun LocationHeader() {
    val context = LocalContext.current //Context is an Android object that gives access to system services, resources, permissions, geocoder, etc.
    
    //state variable to  hold ui data
    var locationText by remember { mutableStateOf("Fetching location...")}
    var hasPermission by remember { 
        mutableStateOf(
            (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) || (
                    ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION ) == PackageManager.PERMISSION_GRANTED
                    )
        )
    }
    
    //Permission launcher contract
    val permissionLauncher = rememberLauncherForActivityResult(  // to handle Android's runtime permission system.
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { 
        permissions -> 
        hasPermission = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true || permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
    }

    LaunchedEffect(hasPermission) {
        if (hasPermission) {
            getCurrentLocation(context) { city ->
                locationText = city
            }
        } else {
            locationText = "Permission required"
        }
    }
    
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Location", color = Color.LightGray, fontSize = 12.sp)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = locationText,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            IconButton(
                onClick = {
                    if (!hasPermission) {
                        permissionLauncher.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                        )
                    } else {
                        // Refresh location
                        getCurrentLocation(context) { city ->
                            locationText = city
                        }
                    }
                }
            ) {
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
}
*/


@Composable
fun LocationHeader() {
    val context = LocalContext.current

    var locationText by remember { mutableStateOf("Fetching location...") }

    val hasPermission = remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
        )
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        hasPermission.value =
            permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                    permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true

        if (hasPermission.value) {
            fetchCurrentCity(context) { city ->
                locationText = city
            }
        } else {
            locationText = "Permission required"
        }
    }
    LaunchedEffect(hasPermission.value) {
        if (hasPermission.value) {
            fetchCurrentCity(context) { city ->
                locationText = city
            }
        } else {
            locationText = "Permission required"
        }
    }
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Location",
            color = Color.LightGray,
            fontSize = 12.sp
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = locationText,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            IconButton(
                onClick = {
                    if (!hasPermission.value) {
                        permissionLauncher.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                        )
                    } else {
                        fetchCurrentCity(context) { city ->
                            locationText = city
                        }
                    }
                }
            ){
                Icon(
                    painter = painterResource(R.drawable.regular_outline_arrow_down),
                    contentDescription = "Refresh location",
                    tint = Color.White,
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .size(16.dp)
                )
            }
        }
    }
}

@SuppressLint("MissingPermission")
private fun fetchCurrentCity(
    context: Context,
    onLocationFetched: (String) -> Unit
){
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    fusedLocationClient.getCurrentLocation(
        Priority.PRIORITY_BALANCED_POWER_ACCURACY,
        CancellationTokenSource().token
    ).addOnSuccessListener { location: Location? ->
        if (location == null) {
            onLocationFetched("Location not available")
            return@addOnSuccessListener
        }
        try {
            if (!Geocoder.isPresent()) {
                onLocationFetched("Geocoder not available")
                return@addOnSuccessListener
            }

            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses = geocoder.getFromLocation(
                location.latitude,
                location.longitude,
                1
            )

            val cityName = addresses?.firstOrNull()?.locality
                ?: addresses?.firstOrNull()?.subAdminArea
                ?: "Unknown Location"

            onLocationFetched(cityName)
        }catch (e: Exception) {
            onLocationFetched("Error fetching city")
        }
    }.addOnFailureListener {
        onLocationFetched("Failed to get location")
    }
}



/*
@SuppressLint("MissingPermission")
private fun getCurrentLocation(
    context: android.content.Context,
    onLocationFetched: (String) -> Unit
) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    fusedLocationClient.lastLocation
        .addOnSuccessListener { location: android.location.Location? ->
            if (location != null) {
                try {
                    val geocoder = Geocoder(context, Locale.getDefault())
                    val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                    val cityName = addresses?.firstOrNull()?.locality ?: "Unknown Location"
                    onLocationFetched(cityName)
                } catch (_: Exception) {
                    onLocationFetched("Error fetching city")
                }
            } else {
                onLocationFetched("Location not available")
            }
        }
        .addOnFailureListener {
            onLocationFetched("Failed to get location")
        }
}
*/