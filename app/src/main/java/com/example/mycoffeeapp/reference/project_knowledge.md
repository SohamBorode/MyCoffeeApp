# MyCoffeeApp Project Knowledge Base

This document provides a comprehensive analysis of the MyCoffeeApp project, including its architecture, file-by-file logic, state management, and future roadmap.

## 1. Project Overview & Architecture

MyCoffeeApp is a modern Android application built using **Jetpack Compose**. It follows a **Clean Architecture** approach with a clear separation of concerns between the Data, UI, and Domain layers.

### Architecture Layers:
- **UI Layer (Jetpack Compose):** Screens, Components, and ViewModels.
- **Data Layer (Repository Pattern):** Retrofit for remote APIs, Repositories for data orchestration, and Mappers for DTO-to-Domain conversion.
- **Navigation (Compose Navigation):** Type-safe navigation using `kotlinx.serialization`.

---

## 2. File-by-File Logic Analysis

### Root
- **`MainActivity.kt`**: Entry point of the app. It sets up the `MyCoffeeAppTheme` and calls `NavGraph()` to handle app-wide navigation.

### Data Layer (`data/`)
#### Remote API (`data/remote/`)
- **`RetrofitClient.kt`**: Singleton object providing Retrofit instances for `CoffeeApiService` and `CartApiService`.
- **`CoffeeApiService.kt`**: Interface defining endpoints for fetching coffee items (`/api/coffee/{id}`, `/api/coffees`).
- **`cart/CartApiService.kt`**: Interface for cart-related operations (`GET`, `POST`, `DELETE`).

#### Repository (`data/repository/`)
- **`CoffeeRepository.kt`**: Orchestrates coffee data. It checks `Constants.USE_BACKEND` to decide between fetching from the API or using `SampleData.kt`.
- **`SampleData.kt`**: Contains hardcoded demo data (`coffeeItemList`) used when the backend is disabled or fails.
- **`cart/CartRepository.kt`**: Manages cart logic, handling fallbacks between `RemoteClassDataSource` and `DemoCartDataSource`.

#### Models & DTOs (`data/model/`)
- **`CoffeeItem.kt`**: Domain model representing a coffee product.
- **`CoffeeCategory.kt`**: Domain model for coffee categories (e.g., "Cappuccino", "Latte").
- **`dto/CoffeeIteDto.kt`**: Data Transfer Object for network responses, annotated with `@Serializable`.
- **`cart/CartItem.kt`**: Model representing an item in the shopping cart.

#### Mappers (`data/mapper/`)
- **`CoffeeMapper.kt`**: Extension functions to convert `CoffeeItemDto` into `CoffeeItem`.

### UI Layer (`ui/`)
#### Navigation (`ui/navigation/`)
- **`NavGraph.kt`**: Main navigation host. Handles the transition from `WelcomeScreen` to the `NavBarGraph`.
- **`NavBarGraph.kt`**: Nested navigation host for the main app sections (Home, Cart, Favorites, Profile). It **initializes and shares ViewModels** across these screens.
- **`NavRoutes.kt` & `NavBarRoutes.kt`**: Define type-safe routes using `Serializable` classes/objects.
- **`NavBarDesign.kt`**: Composable for the Bottom Navigation Bar, managing tab selection and state restoration.

#### Screens (`ui/screens/`)
- **`welcome/WelcomeScreen.kt`**: The onboarding screen with a "Get Started" button.
- **`home/HomeScreen.kt`**: The main landing page. Displays categories, a search bar, banners, and a list of coffee items.
- **`home/HomeViewModel.kt`**: Manages home state, including loading coffee data and filtering by category.
- **`home/CoffeeDetailsScreen.kt`**: Displays detailed info about a selected coffee. Handles size selection, temperature, and "Add to Cart" logic.
- **`cart/CartScreen.kt`**: Lists items in the cart.
- **`cart/CartViewModel.kt`**: Manages cart state (loading, adding, deleting items).
- **`favorite/HeartScreen.kt`**: Placeholder for favorite items.
- **`profile/ProfileScreen.kt`**: Placeholder for user profile.

#### Components (`ui/components/`)
- **`LazyColumnHS.kt`**: A custom wrapper for `LazyColumn` used in the Home screen to handle sticky headers and banners.
- **`CoffeeCard.kt`**: UI component for displaying individual coffee items in a list.
- **`SearchBar.kt` & `ReusableFilterBar.kt`**: Input components for searching and filtering.

---

## 3. Deep Dive into State Management

State in this app is managed using **StateFlow** in ViewModels and **State Hoisting** in Composables.

### State Concepts Used:
1.  **`StateFlow` & `collectAsState()`**:
    - **`HomeViewModel`**: Uses `MutableStateFlow<HomeUiState>` to track the UI state (Loading, Success, Error).
    - **`HomeScreen`**: Collects this flow to reactively update the UI.
2.  **`sealed interface` for UI State**:
    - Used in both `HomeViewModel` and `CartViewModel` to represent exhaustive states (`Loading`, `Success(data)`, `Error(msg)`). This ensures the UI handles all scenarios.
3.  **ViewModel-level Filtering**:
    - `HomeViewModel` maintains `_fullCoffeeList` (raw data) and `_uiState` (filtered data). When `filterByItem(categoryId)` is called, it updates `_uiState` based on the filter.
4.  **Local Composable State (`mutableStateOf`)**:
    - **`CoffeeDetailsScreen`**: Uses a local `state` variable (of type `CoffeeDetailState`) to track user selections like `selectedSize` and `selectedTemperature` before they are sent to the cart. This keeps temporary UI changes isolated.
5.  **ViewModel Sharing**:
    - In `NavBarGraph.kt`, ViewModels are created using `remember` at the NavHost level. This allows `HomeScreen` and `CoffeeDetailsScreen` to share the same `HomeViewModel` instance, ensuring consistency (e.g., favorite status).

---

## 4. Future Proofing

The app is built with several future-proofing strategies:
- **Backend Toggle (`USE_BACKEND`)**: Easily switch between mock data and a real API without changing business logic.
- **Repository Pattern**: Data sources can be swapped (e.g., adding a local Room database) without affecting the UI.
- **Type-Safe Navigation**: Using `Serializable` routes reduces errors during navigation and argument passing.
- **Domain Mappers**: Decouples the UI from the network models, allowing the API to change without breaking the app's internal logic.
- **Modular Components**: Reusable UI parts like `ReusableFilterBar` and `CoffeeCard` make it easy to build new features.

---

## 5. Recommended Next Steps

1.  **Implement Local Storage**:
    - Integrate **Room Database** to cache coffee data and store cart items locally, enabling offline support.
2.  **Complete Placeholders**:
    - Implement the `HeartScreen` (Favorites) using the `isFavorite` flag from `HomeViewModel`.
    - Build out the `ProfileScreen` with user settings.
3.  **Enhance Cart Logic**:
    - Add functionality to increase/decrease item quantities directly in `CartScreen`.
    - Implement a checkout flow.
4.  **Dependency Injection (Hilt)**:
    - Fully utilize **Dagger Hilt** (currently partially present in `data/hilt`) to manage ViewModel and Repository injection, removing manual `remember` calls in `NavBarGraph`.
5.  **Search Functionality**:
    - Connect the `SearchBar` in `HomeScreen` to the `HomeViewModel` to filter items by name in real-time.
