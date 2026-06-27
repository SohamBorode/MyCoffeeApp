package com.example.mycoffeeapp.ui.screens.profile


import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.mycoffeeapp.R
import com.example.mycoffeeapp.ui.navigation.NavBarDesign
import com.example.mycoffeeapp.ui.navigation.NavBarRoutes
import com.example.mycoffeeapp.ui.theme.CafeCream
import com.example.mycoffeeapp.ui.theme.CafeTextDark
import com.example.mycoffeeapp.ui.theme.CafeTextGray
import com.example.mycoffeeapp.ui.theme.OffWhite
import com.example.mycoffeeapp.ui.theme.PureWhite
import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavHostController,
    viewModel: ProfileViewModel,
    authViewModel: Any
) {
    val state by viewModel.uiState.collectAsState()
    var showImageMenu by remember { mutableStateOf(false) }

    val activeSheet by viewModel.activeSheet.collectAsState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)

    val accountState by viewModel.accountUiState.collectAsState()

    /// camera
    val context = LocalContext.current

    // Add the permission launcher
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Navigate to camera if permission is granted after request
            navController.navigate(NavBarRoutes.CameraPreview)
        } else {
            // Optionally handle permission denial (e.g., show a Toast)
        }
    }

    Scaffold(
        containerColor = CafeCream,
        bottomBar = { NavBarDesign(navController, "ProfileScreen") }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // Circular Back Button
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .size(40.dp)
                    .background(PureWhite, shape = CircleShape)
                    .border(1.dp, Color.LightGray.copy(alpha = 0.3f), CircleShape)
            ) {
                Icon(
                    painter = painterResource(R.drawable.regular_outline_arrow_left),
                    contentDescription = "Back",
                    modifier = Modifier.size(20.dp),
                    tint = CafeTextDark
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Title Section
            Text(
                text = "My",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = CafeTextDark
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Profile",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = CafeTextDark
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    painter = painterResource(R.drawable.outline_account_circle_24),
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    tint = CafeTextDark
                )
            }

            Spacer(modifier = Modifier.height(32.dp))


            when (val profileState = state) {
                is ProfileUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = CafeTextDark)
                    }
                }

                is ProfileUiState.Error -> {
                    Text(
                        text = (state as ProfileUiState.Error).msg,
                        modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
                        color = CafeTextDark
                    )
                }

                is ProfileUiState.Success -> {
                    // Profile Image Section
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            contentAlignment = Alignment.BottomEnd
                        ) {
                            // Main Profile Image with shadow and border to soften the edges
                            AsyncImage(
                                model = profileState.profileImageUri,
                                contentDescription = "Profile Picture",
                                placeholder = painterResource(profileState.defaultProfileImage),
                                error = painterResource(profileState.defaultProfileImage),
                                modifier = Modifier
                                    .size(130.dp)
                                    .shadow(elevation = 6.dp, shape = CircleShape)
                                    .border(4.dp, PureWhite, CircleShape)
                                    .clip(CircleShape)
                                    .clickable {/*On click the image opens the image window*/ },
                                contentScale = ContentScale.Crop
                            )

                            // Camera Action Icon as a Badge
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .background(PureWhite, CircleShape)
                                    .border(1.dp, Color.LightGray.copy(alpha = 0.4f), CircleShape)
                                    .clip(CircleShape)
                                    .clickable {
                                        showImageMenu = true
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.photo_camera_icon),
                                    contentDescription = "Change Picture",
                                    modifier = Modifier.size(20.dp),
                                    tint = CafeTextDark
                                )
                                DropdownMenu(
                                    expanded = showImageMenu,
                                    onDismissRequest = {
                                        showImageMenu = false
                                    },
                                    Modifier.background(color = OffWhite)
                                ) {
                                    DropdownMenuItem(
                                        text = { Text("Camera") },
                                        onClick = {
                                            showImageMenu = false
                                            val permissionCheckResult =
                                                ContextCompat.checkSelfPermission(
                                                    context,
                                                    Manifest.permission.CAMERA
                                                )

                                            if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                                                // Already have permission, navigate immediately
                                                navController.navigate(NavBarRoutes.CameraPreview)
                                            } else {
                                                // Request permission
                                                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                                            }
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = { Text("Gallery") },
                                        onClick = {
                                            showImageMenu = false
                                            // launch gallery
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = { Text("Remove") },
                                        onClick = {
                                            showImageMenu = false
                                            viewModel.removeProfileImage()
                                        }
                                    )
                                }
                            }
                        }
                    }
                    /*Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // Camera Action Icon
                            Box(
                                modifier = Modifier
                                    .size(45.dp)
                                    .border(1.dp, CafeTextDark, CircleShape)
                                    .clip(CircleShape)
                                    .clickable { /* End Point: Image Picker */ },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.photo_camera_icon),
                                    contentDescription = "Change Picture",
                                    modifier = Modifier.size(20.dp),
                                    tint = CafeTextDark
                                )
                            }

                            Spacer(modifier = Modifier.width(20.dp))

                            // Main Profile Image
                            Image(
                                painter = painterResource(profileState.profileImg),
                                contentDescription = "Profile Picture",
                                modifier = Modifier
                                    .size(120.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
*/
                    Spacer(modifier = Modifier.height(16.dp))

                    // Username
                    Text(
                        text = profileState.username,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = CafeTextDark,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    Spacer(modifier = Modifier.height(40.dp))

                    // Menu Options (End Points for Navigation)
                    ProfileMenuItem(
                        title = "My Account",
                        onClick = { viewModel.showBottonSheet(ProfileSheetType.ACCOUNT) })
                    ProfileMenuItem(title = "Orders", onClick = {
                        viewModel.showBottonSheet(
                            ProfileSheetType.ORDERS
                        )
                    })
                    ProfileMenuItem(
                        title = "Terms and Conditions",
                        onClick = { viewModel.showBottonSheet(ProfileSheetType.TERMS) })
                    ProfileMenuItem(
                        title = "Help Center",
                        onClick = { viewModel.showBottonSheet(ProfileSheetType.HELP) })

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }


        }
    }

    if (activeSheet != null) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.showBottonSheet(null) },
            sheetState = sheetState,
            containerColor = CafeCream,
            dragHandle = { BottomSheetDefaults.DragHandle() }
        ) {
           when(activeSheet){
               ProfileSheetType.ACCOUNT -> AccountSheetContent(accountState)
               ProfileSheetType.ORDERS -> OrdersSheetContent()
               ProfileSheetType.TERMS -> TermsSheetContent()
               ProfileSheetType.HELP -> HelpCenterSheetContent()
               else -> {}
           }
        }
    }
}

@Composable
fun ProfileMenuItem(title: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = PureWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 20.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = CafeTextDark
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = CafeTextGray
            )
        }
    }
}

/*
@Composable
fun ProfileScreen(navController: NavHostController) {
    Scaffold(
        containerColor = Color(0xFFF7F1EA),
        bottomBar = { NavBarDesign(navController, "ProfileScreen") }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxSize(1f / 5f)
                    .padding(10.dp)
            ) {
                IconButton(
                    onClick = {},
                ) {
                    Icon(
                        painter = painterResource(R.drawable.regular_outline_arrow_left),
                        contentDescription = "Back to Home page", modifier = Modifier
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "My",
                    color = Black,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 35.sp
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Profile",
                        color = Black,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 35.sp
                    )
                    Icon(
                        painter = painterResource(R.drawable.outline_account_circle_24),
                        contentDescription = null,
                    )
                }
                Spacer(modifier = Modifier.height(40.dp))
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth().padding(30.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = {}) {
                            Icon(
                                painter = painterResource(R.drawable.regular_outline_bag),
                                contentDescription = "Camera icon to change profile picture"
                            )
                        }
                        Spacer(modifier = Modifier.width(30.dp))
                        Image(
                            painter = painterResource(R.drawable.coffee_1),
                            contentDescription = "Profile image",
                            modifier = Modifier
                                .size(100.dp)
                                .clip(shape = CircleShape)
                                .border(border =  BorderStroke(2.dp, color = Color.Transparent))
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "Username",
                        color = Black,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 30.sp
                    )

                }

            }

        }
    }
}
*/
