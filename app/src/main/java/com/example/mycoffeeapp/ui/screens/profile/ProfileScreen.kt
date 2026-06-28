package com.example.mycoffeeapp.ui.screens.profile

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
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
import com.example.mycoffeeapp.ui.screens.loginsignp.AuthViewModel
import com.example.mycoffeeapp.ui.theme.CafeCream
import com.example.mycoffeeapp.ui.theme.CafeTextDark
import com.example.mycoffeeapp.ui.theme.CafeTextGray
import com.example.mycoffeeapp.ui.theme.OffWhite
import com.example.mycoffeeapp.ui.theme.PureWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavHostController,
    viewModel: ProfileViewModel,
    authViewModel: AuthViewModel,
    onLogoutSuccess: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val activeSheet by viewModel.activeSheet.collectAsState()
    val accountState by viewModel.accountUiState.collectAsState()
    val orderState by viewModel.orderUiState.collectAsState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    var showImageMenu by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) navController.navigate(NavBarRoutes.CameraPreview)
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        viewModel.onProfileImageSelected(uri?.toString())
    }

    Scaffold(
        containerColor = CafeCream,
        bottomBar = { NavBarDesign(navController, NavBarRoutes.ProfileScreen) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(20.dp))

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

            Text(text = "My", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = CafeTextDark)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Profile", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = CafeTextDark)
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
                        text = profileState.msg,
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        color = CafeTextDark
                    )
                }

                is ProfileUiState.Success -> {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Box(contentAlignment = Alignment.BottomEnd) {
                            AsyncImage(
                                model = profileState.profileImageUri,
                                contentDescription = "Profile Picture",
                                placeholder = painterResource(profileState.defaultProfileImage),
                                error = painterResource(profileState.defaultProfileImage),
                                modifier = Modifier
                                    .size(130.dp)
                                    .shadow(elevation = 6.dp, shape = CircleShape)
                                    .border(4.dp, PureWhite, CircleShape)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )

                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .background(PureWhite, CircleShape)
                                    .border(1.dp, Color.LightGray.copy(alpha = 0.4f), CircleShape)
                                    .clip(CircleShape)
                                    .clickable { showImageMenu = true },
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
                                    onDismissRequest = { showImageMenu = false },
                                    modifier = Modifier.background(color = OffWhite)
                                ) {
                                    DropdownMenuItem(
                                        text = { Text("Camera") },
                                        onClick = {
                                            showImageMenu = false
                                            val permissionCheckResult = ContextCompat.checkSelfPermission(
                                                context,
                                                Manifest.permission.CAMERA
                                            )
                                            if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                                                navController.navigate(NavBarRoutes.CameraPreview)
                                            } else {
                                                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                                            }
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = { Text("Gallery") },
                                        onClick = {
                                            showImageMenu = false
                                            galleryLauncher.launch(
                                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                            )
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

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = profileState.username,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = CafeTextDark,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    ProfileMenuItem("My Account") { viewModel.showBottonSheet(ProfileSheetType.ACCOUNT) }
                    ProfileMenuItem("Orders") { viewModel.showBottonSheet(ProfileSheetType.ORDERS) }
                    ProfileMenuItem("Terms and Conditions") { viewModel.showBottonSheet(ProfileSheetType.TERMS) }
                    ProfileMenuItem("Help Center") { viewModel.showBottonSheet(ProfileSheetType.HELP) }

                    Button(
                        onClick = {
                            authViewModel.logout()
                            onLogoutSuccess()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    ) {
                        Text("Logout")
                    }

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
            when (activeSheet) {
                ProfileSheetType.ACCOUNT -> AccountSheetContent(accountState)
                ProfileSheetType.ORDERS -> OrdersSheetContent(orderState)
                ProfileSheetType.TERMS -> TermsSheetContent()
                ProfileSheetType.HELP -> HelpCenterSheetContent()
                null -> Unit
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
