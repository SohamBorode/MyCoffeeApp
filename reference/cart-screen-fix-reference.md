# Cart Screen Fix Reference

This file contains the fixed code for the cart screen redesign and the related compatibility updates.

## 1) `CartItem.kt`

Path:
`app/src/main/java/com/example/mycoffeeapp/data/model/cart/CartItem.kt`

```kotlin
package com.example.mycoffeeapp.data.model.cart

data class CartItem(
    val id: String,
    val coffeeId: String,
    val name: String,
    val imageUrl: String,
    val price: Double,
    val size: String,
    val temperature: String,
    val quantity: Int = 1,
    val subtitle: String = ""
)
```

## 2) `CartViewModel.kt`

Path:
`app/src/main/java/com/example/mycoffeeapp/ui/screens/cart/CartViewModel.kt`

```kotlin
package com.example.mycoffeeapp.ui.screens.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mycoffeeapp.data.model.CoffeeItem
import com.example.mycoffeeapp.data.model.cart.CartItem
import com.example.mycoffeeapp.data.repository.cart.CartRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CartSummary(
    val subtotal: Double,
    val deliveryFee: Double,
    val total: Double
)

sealed interface CartUiState {
    data object Loading : CartUiState
    data class Success(
        val cartCoffeeList: List<CartItem>,
        val summary: CartSummary
    ) : CartUiState
    data class Error(val msg: String) : CartUiState
}

class CartViewModel(
    private val cartRepository: CartRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<CartUiState>(CartUiState.Loading)
    val uiState: StateFlow<CartUiState> = _uiState.asStateFlow()

    val cartCount: Int
        get() = (uiState.value as? CartUiState.Success)
            ?.cartCoffeeList
            ?.sumOf { it.quantity }
            ?: 0

    init {
        loadCartData()
    }

    fun loadCartData() {
        viewModelScope.launch {
            _uiState.value = CartUiState.Loading
            runCatching {
                cartRepository.getCartItems()
            }.onSuccess { items ->
                _uiState.value = CartUiState.Success(
                    cartCoffeeList = items,
                    summary = calculateSummary(items)
                )
            }.onFailure { err ->
                _uiState.value = CartUiState.Error(
                    err.localizedMessage ?: "Unknown network error"
                )
            }
        }
    }

    fun addToCart(
        coffeeItem: CoffeeItem,
        size: String,
        temperature: String,
        finalPrice: Double = coffeeItem.price
    ) {
        viewModelScope.launch {
            runCatching {
                val currentItems = cartRepository.getCartItems()
                val existing = currentItems.firstOrNull {
                    it.coffeeId == coffeeItem.id &&
                        it.size == size &&
                        it.temperature == temperature
                }

                if (existing != null) {
                    cartRepository.updateCartItem(
                        existing.copy(
                            quantity = existing.quantity + 1,
                            price = finalPrice
                        )
                    )
                } else {
                    cartRepository.addToCart(
                        CartItem(
                            id = java.util.UUID.randomUUID().toString(),
                            coffeeId = coffeeItem.id,
                            name = coffeeItem.name,
                            imageUrl = coffeeItem.imageUrl,
                            price = finalPrice,
                            size = size,
                            temperature = temperature,
                            quantity = 1
                        )
                    )
                }
            }.onSuccess {
                loadCartData()
            }.onFailure { err ->
                _uiState.value = CartUiState.Error(
                    err.localizedMessage ?: "Unable to update cart"
                )
            }
        }
    }

    fun increaseQuantity(itemId: String) {
        changeQuantity(itemId, +1)
    }

    fun decreaseQuantity(itemId: String) {
        changeQuantity(itemId, -1)
    }

    fun deleteItem(id: String) {
        viewModelScope.launch {
            runCatching {
                cartRepository.deleteCartItem(id)
            }.onSuccess {
                loadCartData()
            }.onFailure { err ->
                _uiState.value = CartUiState.Error(
                    err.localizedMessage ?: "Unable to delete item"
                )
            }
        }
    }

    private fun changeQuantity(itemId: String, delta: Int) {
        viewModelScope.launch {
            runCatching {
                val currentItems = cartRepository.getCartItems()
                val target = currentItems.firstOrNull { it.id == itemId } ?: return@runCatching

                val newQuantity = target.quantity + delta
                if (newQuantity > 0) {
                    cartRepository.updateCartItem(
                        target.copy(quantity = newQuantity)
                    )
                } else {
                    cartRepository.deleteCartItem(itemId)
                }
            }.onSuccess {
                loadCartData()
            }.onFailure { err ->
                _uiState.value = CartUiState.Error(
                    err.localizedMessage ?: "Unable to update quantity"
                )
            }
        }
    }

    private fun calculateSummary(items: List<CartItem>): CartSummary {
        val subtotal = items.sumOf { it.price * it.quantity }
        val deliveryFee = if (items.isNotEmpty()) 1.0 else 0.0
        return CartSummary(
            subtotal = subtotal,
            deliveryFee = deliveryFee,
            total = subtotal + deliveryFee
        )
    }
}
```

## 3) `CartScreen.kt`

Path:
`app/src/main/java/com/example/mycoffeeapp/ui/screens/cart/CartScreen.kt`

```kotlin
package com.example.mycoffeeapp.ui.screens.cart

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
                                    subtotal = state.summary.subtotal,
                                    deliveryFee = state.summary.deliveryFee,
                                    total = state.summary.total,
                                    onPlaceOrderClick = {
                                    }
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
                    text = item.subtitle.ifBlank { "${item.temperature} • ${item.size}" },
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
                        painter = painterResource(R.drawable.regular_outline_trash),
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
private fun StepButton(text: String,onClick: () -> Unit) {
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

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFDCD6D0)),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.mobile_banking),
                        contentDescription = "Payment method",
                        tint = CafeBrown,
                        modifier = Modifier.size(20.dp)
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Online",
                            color = CafeTextDark,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = money(total),
                            color = CafeBrown,
                            fontSize = 12.sp
                        )
                    }

                    Icon(
                        painter = painterResource(R.drawable.regular_outline_arrow_down),
                        contentDescription = "Expand payment methods",
                        tint = CafeTextDark,
                        modifier = Modifier.size(18.dp)
                    )
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
        onPlaceOrderClick = {}
    )
}
```

## 4) `NavBarGraph.kt`

Path:
`app/src/main/java/com/example/mycoffeeapp/ui/navigation/NavBarGraph.kt`

Replace:

```kotlin
val cartCount = (cartState as? CartUiState.Success)?.cartCoffeeList?.size ?: 0
```

With:

```kotlin
val cartCount = (cartState as? CartUiState.Success)
    ?.cartCoffeeList
    ?.sumOf { it.quantity }
    ?: 0
```

## 5) `CartRepository.kt`

Path:
`app/src/main/java/com/example/mycoffeeapp/data/repository/cart/CartRepository.kt`

Add this contract if it does not already exist:

```kotlin
suspend fun updateCartItem(cartItem: CartItem)
```

If you use remote/demo data sources, add the same update method there too.

## 6) Notes

- The cart should merge duplicate items by `coffeeId + size + temperature`.
- Quantity updates must persist through the repository layer.
- `Place Order` is only a UI action right now; connect it to checkout later.
- The badge count should represent total quantity, not number of rows.


```kotlin
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// 1. Define your payment options data model
data class PaymentMethod(val id: String, val name: String, val iconRes: Int?)

@Composable
fun PaymentSelectorCard() {
// UI Theme Colors (Matching your dark mode image)
val cardBackground = Color(0xFF1E1E1E) // Exact dark gray from your block
val accentOrange = Color(0xFFFF6B00)  // Matching your "Place Order" color
val textWhite = Color(0xFFFFFFFF)
val textMuted = Color(0xFF8E8E93)
val dividerColor = Color(0xFF2C2C2E)

// State management  
    var showPaymentOptions by remember { mutableStateOf(false) }

    val paymentOptions = remember {
        listOf(
            PaymentMethod("netBanking", "Net Banking", R.drawable.net_banking),
            PaymentMethod("card", "Credit / Debit Card", R.drawable.debit_card), // Replace null with your R.drawable.ic_card
            PaymentMethod("upi", "Upi Payment",R.drawable.upi),
            PaymentMethod("digiWallet", "Digital Wallet", R.drawable.digital_wallet),
            PaymentMethod("crypto", "Cryptocurrency", R.drawable.crypto),
            PaymentMethod("cashOnDelivery", "Cash On Delivery", R.drawable.cash_on_delivery),
            PaymentMethod("prepaid", "Prepaid / Gift Card", R.drawable.gift_card),
            PaymentMethod("bnpl", "Buy Now Pay Later", R.drawable.pay_later)
        )
    }
    var selectedPaymentMode by remember { mutableStateOf (paymentOptions[0])}
    val arrowRotationAngle by animateFloatAsState(
        targetValue = if (showPaymentOptions) 180f else 0f,
        label = "ArrowRotation"
    )

// Main parent container box  
Card(  
    modifier = Modifier  
        .fillMaxWidth()  
        .padding(16.dp),  
    shape = RoundedCornerShape(12.dp),  
    colors = CardDefaults.cardColors(containerColor = cardBackground)  
) {  
    Column(modifier = Modifier.fillMaxWidth()) {  
          
        // Header Header Row: Displays current selection & contains toggle target  
        Row(  
            modifier = Modifier  
                .fillMaxWidth()  
                .clickable { isExpanded = !isExpanded } // Toggles expansion anywhere on row  
                .padding(horizontal = 16.dp, vertical = 20.dp),  
            verticalAlignment = Alignment.CenterVertically,  
            horizontalArrangement = Arrangement.SpaceBetween  
        ) {  
            Column {  
                Text(  
                    text = "Payment Method",  
                    color = textMuted,  
                    fontSize = 12.sp,  
                    fontWeight = FontWeight.Medium  
                )  
                Spacer(modifier = Modifier.height(4.dp))  
                Text(  
                    text = selectedMethod.name,  
                    color = if (selectedMethod.id == "online") accentOrange else textWhite,  
                    fontSize = 16.sp,  
                    fontWeight = FontWeight.Bold  
                )  
            }  

            // Smoothly animated arrow vector  
            Icon(  
                imageVector = Icons.Default.KeyboardArrowDown,  
                contentDescription = "Toggle Payment Options",  
                tint = textWhite,  
                modifier = Modifier  
                    .size(24.dp)  
                    .rotate(arrowRotationAngle)  
            )  
        }  

        // 2. Animated Slide-Down Dropdown Menu Extension  
        AnimatedVisibility(  
            visible = isExpanded,  
            enter = expandVertically() + fadeIn(),  
            exit = shrinkVertically() + fadeOut()  
        ) {  
            Column(modifier = Modifier.fillMaxWidth()) {  
                paymentOptions.forEach { method ->  
                    // Skip rendering the currently active option in the sub-list if desired,   
                    // or keep it to show full configuration options.  
                      
                    HorizontalDivider(color = dividerColor, thickness = 1.dp)  

                    Row(  
                        modifier = Modifier  
                            .fillMaxWidth()  
                            .clickable {  
                                selectedMethod = method  
                                isExpanded = false // Closes cleanly on choice selection  
                            }  
                            .padding(horizontal = 16.dp, vertical = 16.dp),  
                        verticalAlignment = Alignment.CenterVertically,  
                        horizontalArrangement = Arrangement.SpaceBetween  
                    ) {  
                        Row(verticalAlignment = Alignment.CenterVertically) {  
                            // Dynamic placeholder logic for your payment icon assets  
                            if (method.iconRes != null) {  
                                Icon(  
                                    painter = painterResource(id = method.iconRes),  
                                    contentDescription = null,  
                                    tint = textWhite,  
                                    modifier = Modifier.size(24.dp).padding(end = 12.dp)  
                                )  
                            }  
                            Text(  
                                text = method.name,  
                                color = textWhite,  
                                fontSize = 15.sp,  
                                fontWeight = FontWeight.Normal  
                            )  
                        }  

                        // Selection state indicator circle  
                        RadioButton(  
                            selected = (method.id == selectedMethod.id),  
                            onClick = {  
                                selectedMethod = method  
                                isExpanded = false  
                            },  
                            colors = RadioButtonDefaults.colors(  
                                selectedColor = accentOrange,  
                                unselectedColor = textMuted  
                            )  
                        )  
                    }  
                }  
            }  
        }  
    }  
}

}
```