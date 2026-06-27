# Explanation of ViewModels in MyCoffeeApp

This document provides a detailed yet simple explanation of how the ViewModels in **MyCoffeeApp** work. ViewModels are responsible for managing the UI state and handling the logic for each screen, ensuring that data is preserved even when the screen rotates or changes.

---

## 1. HomeViewModel
**Path:** `com.example.mycoffeeapp.ui.screens.home.HomeViewModel`

### Purpose:
The `HomeViewModel` is the "brain" of the home screen. It handles showing the list of available coffees, searching for specific items, and filtering them by category (like Cappuccino, Latte, etc.).

### Key Working Parts:
- **`HomeUiState`**: This tells the screen what to show. It can be `Loading` (showing a spinner), `Success` (showing the coffee list), or `Error` (showing an error message).
- **`loadCoffeeData()`**: Fetches all coffee items from the repository when the app starts.
- **Filtering**: When you click a category (like "Cold Brew"), `filterByItem(categoryId)` is called. It filters the main list to show only items matching that category.
- **Searching**: When you type in the search bar, `onSearchQueryChange(query)` waits for a split second (debouncing) and then asks the repository for matching coffees.
- **Favorites**: It syncs with the `FavoriteRepository` so that if you "heart" a coffee on the home screen, it updates everywhere.

---

## 2. CartViewModel
**Path:** `com.example.mycoffeeapp.ui.screens.cart.CartViewModel`

### Purpose:
This ViewModel manages your shopping cart. It tracks what you want to buy, the quantity of each item, and handles the checkout process.

### Key Working Parts:
- **`CartUiState`**: Manages the list of items in the cart, the price summary, and the selected payment method.
- **`addToCart(...)`**: Adds a new coffee to the cart. If the same coffee (with the same size and temperature) is already there, it just increases the quantity.
- **Price Calculation**: The `calculateSummary()` function automatically adds up the prices of all items and adds a delivery fee (if the cart isn't empty) to give you the total.
- **Quantity Control**: `increaseQuantity` and `decreaseQuantity` allow you to change how many of each item you want. If quantity reaches zero, the item is removed.
- **Payment Selection**: It holds a list of payment methods (UPI, Card, Cash on Delivery, etc.) and tracks which one the user has picked.
- **Order Flow**: `placeOrder` and `confirmOrder` handle the transition from viewing the cart to finishing the purchase and clearing the cart.

---

## 3. FavoriteViewModel
**Path:** `com.example.mycoffeeapp.ui.screens.favorite.FavoriteViewModel`

### Purpose:
A dedicated ViewModel for the "Favorites" screen. Its only job is to show the list of coffee items the user has liked.

### Key Working Parts:
- **`applyFavoriteList()`**: This is the core logic. It takes the full list of all coffees and filters them by checking which ones have their IDs saved in the `FavoriteRepository`.
- **Real-time Updates**: It listens (collects) to the `favoriteIds` flow. This means if you remove a favorite from this screen, the list updates instantly without needing to refresh.
- **`toggleFavorite(coffeeId)`**: Allows the user to "unfavorite" an item directly from the favorites screen.

---

## 4. ProfileViewModel
**Path:** `com.example.mycoffeeapp.ui.screens.profile.ProfileViewModel`

### Purpose:
Manages the user's personal information, profile picture, and history (like past orders).

### Key Working Parts:
- **Profile Image**: Handles picking a new image (from camera or gallery) or removing the current one. It tells the `ProfileRepository` to save these changes.
- **Bottom Sheets**: The profile screen has several sections like "Account Details", "My Orders", and "Help". This ViewModel uses `activeSheet` to track which one should be visible.
- **`loadAccountDetails()`**: Fetches detailed user info (Email, Phone, DOB, etc.) only when the user opens the Account section, saving resources.
- **Orders**: `loadOrders()` fetches the history of what the user has bought in the past.

---

## 5. AuthViewModel
**Path:** `com.example.mycoffeeapp.ui.screens.loginsignp.LoginSignUpViewModel`

### Purpose:
Handles the "Gatekeeping" of the app—letting users in (Login/Signup) and letting them out (Logout).

### Key Working Parts:
- **`login(email, password)`**: Sends credentials to the `AuthRepository`. If successful, it uses `SessionManager` to "remember" the user so they don't have to log in every time the app opens.
- **`signup(...)`**: Creates a new account with a name, email, and password.
- **Session Management**: It works closely with `SessionManager` to save or clear the user's login token.
- **`logout()`**: Clears all saved session data and returns the app to the login screen state.

---

### Summary Table

| ViewModel | Main Responsibility | Key Feature |
| :--- | :--- | :--- |
| **Home** | Discovery | Searching and Category Filtering |
| **Cart** | Purchasing | Price calculation and Payment selection |
| **Favorite** | Personalization | Showing liked items |
| **Profile** | User Management | Profile updates and Order history |
| **Auth** | Security | Login, Signup, and Session saving |
