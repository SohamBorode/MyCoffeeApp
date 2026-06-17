package com.example.mycoffeeapp.ui.screens.cart

import android.provider.MediaStore
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.mycoffeeapp.R
import com.example.mycoffeeapp.data.model.cart.CartItem
import com.example.mycoffeeapp.ui.navigation.NavBarDesign
import com.example.mycoffeeapp.ui.theme.CafeBrown
import com.example.mycoffeeapp.ui.theme.CafeTextDark
import com.example.mycoffeeapp.ui.theme.CafeTextGray
import com.example.mycoffeeapp.ui.theme.Gray
import com.example.mycoffeeapp.ui.theme.LightGray
import com.example.mycoffeeapp.ui.theme.PureWhite
import java.text.NumberFormat
import java.util.Locale

@Composable
fun CartScreen(
    onBackClick: () -> Unit,
    navController: NavHostController,
    viewModel: CartViewModel = viewModel(),
    cartCount: Int
) {
    val cartState by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = Color(0xFFF7F1EA),
        bottomBar = { NavBarDesign(navController, "CartScreen", cartCount) },
        topBar = {
            CartHeaderSection(
                title = "Order",
                onBackClick = onBackClick,
                onFavoriteClick = { }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val state = cartState) {
                is CartUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                is CartUiState.Error -> {
                    Text(
                        text = state.msg,
                        modifier = Modifier.align(Alignment.Center),
                        color = CafeTextDark
                    )
                }

                is CartUiState.Success -> {
                    if (state.cartCoffeeList.isEmpty()) {
                        EmptyCartView()
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(
                                start = 16.dp,
                                end = 16.dp,
                                top = 8.dp,
                                bottom = 16.dp
                            ),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            item {
                                Text(
                                    text = "Deliver",
                                    color = CafeBrown,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            items(state.cartCoffeeList, key = { it.id }) { cartCoffeeItem ->
                                CartCoffeeItem(
                                    item = cartCoffeeItem,
                                    onDecrease = { viewModel.decreaseQuantity(cartCoffeeItem.id) },
                                    onIncrease = { viewModel.increaseQuantity(cartCoffeeItem.id) },
                                    onDeleteClick = { viewModel.deleteItem(cartCoffeeItem.id) }
                                )
                            }

                            item {
                                Spacer(modifier = Modifier.height(4.dp))
                                PaymentSummaryCard(
                                    subtotal = state.summary.subTotal,
                                    deliveryFee = state.summary.deliveryFee,
                                    total = state.summary.total,
                                    paymentMethods = state.paymentMethod,
                                    selectedPaymentMethod = state.selectedPaymentMethod,
                                    expand = state.isPaymentCardExpanded,
                                    onExpandClick = {
                                        viewModel.togglePaymentMethod()
                                    },
                                    onPaymentSelected = {
                                        viewModel.selectPayment(it)
                                    },
                                    onPlaceOrderClick = { }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CartHeaderSection(
    title: String,
    onBackClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.regular_outline_arrow_left),
                contentDescription = "Back",
                tint = CafeTextDark
            )
        }

        Text(
            text = title,
            modifier = Modifier.align(Alignment.Center),
            color = CafeTextDark,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        IconButton(
            onClick = onFavoriteClick,
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "Favorite",
                tint = CafeTextDark
            )
        }
    }
}

@Composable
fun CartCoffeeItem(
    item: CartItem,
    onDecrease: () -> Unit,
    onIncrease: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE7E1DB)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = if (item.imageUrl.isBlank()) R.drawable.coffee_5 else item.imageUrl,
                contentDescription = item.name,
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(14.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = item.name,
                    color = CafeTextDark,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = item.subtitle.ifBlank { "${item.temperature} • ${item.size} • ${item.price}/pc" },
                    color = CafeTextGray,
                    fontSize = 12.sp
                )
            }

            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                QuantityStepper(
                    quantity = item.quantity,
                    onDecrease = onDecrease,
                    onIncrease = onIncrease
                )

                IconButton(
                    onClick = onDeleteClick,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = CafeBrown
                    )
                }
            }
        }
    }
}

@Composable
private fun QuantityStepper(
    quantity: Int,
    onDecrease: () -> Unit,
    onIncrease: () -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        StepButton(text = "−", onClick = onDecrease)
        Text(
            text = quantity.toString(),
            modifier = Modifier.padding(horizontal = 10.dp),
            color = CafeTextDark,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
        StepButton(text = "+", onClick = onIncrease)
    }
}

@Composable
private fun StepButton(text: String, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF3ECE4)),
        modifier = Modifier.size(28.dp),
        onClick = onClick
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = CafeBrown,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun PaymentSummaryCard(
    subtotal: Double,
    deliveryFee: Double,
    total: Double,
    paymentMethods: List<PaymentMethod>,
    selectedPaymentMethod: PaymentMethod,
    expand: Boolean,
    onExpandClick: () -> Unit,
    onPaymentSelected: (PaymentMethod) -> Unit,
    onPlaceOrderClick: () -> Unit
) {


    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE7E1DB)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Payment Summary",
                color = CafeTextDark,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            SummaryRow(label = "Price", value = money(subtotal))
            SummaryRow(label = "Delivery Fee", value = money(deliveryFee))

            // payment option card and selection
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFDCD6D0)),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 14.dp, top = 14.dp, bottom = 14.dp, end = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(selectedPaymentMethod.iconRes),
                        contentDescription = "Payment method",
                        tint = CafeBrown,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = selectedPaymentMethod.title,
                            color = CafeTextDark,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = money(total),
                            color = CafeBrown,
                            fontSize = 12.sp
                        )
                    }
                    IconButton(
                        onClick = onExpandClick,
                        modifier = Modifier
                            .size(25.dp)
                            .clip(shape = RoundedCornerShape(50.dp))
                            .background(color = Gray),

                        ) {
                        Icon(
                            painter = painterResource(R.drawable.regular_outline_arrow_down),
                            contentDescription = "Expand payment methods",
                            tint = CafeTextDark,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    // Aniamtion expanindng
                    AnimatedVisibility(
                        visible = true,
                        enter = expandVertically(animationSpec = tween(250)) + fadeIn(
                            animationSpec = tween(
                                250
                            )
                        ) + slideInVertically(
                            animationSpec = tween(250),
                            initialOffsetY = { it / 2 }),
                        exit = shrinkVertically(animationSpec = tween(200)) + fadeOut(
                            animationSpec = tween(
                                150
                            )
                        ) + slideOutVertically(
                            animationSpec = tween(
                                200
                            ), targetOffsetY = { it / 2 }),
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            paymentMethods.forEachIndexed { index, method ->
                                if (index != 0) {
                                    HorizontalDivider(
                                        color = LightGray, thickness = 1.dp
                                    )
                                }
                                PaymentOptionRow(
                                    method = method,
                                    selected = (method.id == selectedPaymentMethod.id),
                                    onClick = { onPaymentSelected(method) }
                                )
                            }
                        }
                    }


                }
            }

            Button(
                onClick = onPlaceOrderClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CafeBrown,
                    contentColor = PureWhite
                )
            ) {
                Text(
                    text = "Place Order",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}


@Composable
fun PaymentOptionRow(selected: Boolean, method: PaymentMethod, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 6.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(method.iconRes),
            contentDescription = method.title,
            tint = CafeBrown,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))

        Text(
            text = method.title,
            color = CafeTextDark,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )
        RadioButton(
            selected = selected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor =  CafeBrown,
                unselectedColor = CafeTextGray
            )
        )
    }
}

@Composable
private fun SummaryRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            color = CafeTextDark,
            fontSize = 14.sp
        )
        Text(
            text = value,
            color = CafeTextDark,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun EmptyCartView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Your cart is empty",
                color = CafeTextDark,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Add items to see them here",
                color = CafeTextGray,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

private fun money(value: Double): String {
    return NumberFormat.getCurrencyInstance(Locale.US).format(value)
}

@Preview(showBackground = true)
@Composable
fun PreviewPaymentCard() {
    PaymentSummaryCard(
        subtotal = 12.50,
        deliveryFee = 1.00,
        total = 13.50,
        selectedPaymentMethod = TODO(),
        paymentMethods = TODO(),
        expand = TODO(),
        onExpandClick = TODO(),
        onPaymentSelected = TODO(),
        onPlaceOrderClick = {},
    )
}