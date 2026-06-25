# Backend Readiness: The Ultimate Implementation Guide

This document outlines the exact changes and new files needed to transition the app from "Demo Mode" to a "Production Backend" connection.

## 1. Core API Fixes (Asynchronous Support)
**File**: `com.example.mycoffeeapp.data.remote.auth.AuthApiService`

The `AuthApiService` must use the `suspend` keyword. Network calls are heavy and must not block the UI thread.

```kotlin
interface AuthApiService {
    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): AuthSession

    @POST("auth/signup")
    suspend fun signup(@Body signupRequest: SignupRequest): AuthSession

    @GET("auth/me")
    suspend fun getCurrentUser(): AppUser

    @POST("auth/logout")
    suspend fun logout()
}
```

---

## 2. Session Management (Persistent Token)
**New File**: `com.example.mycoffeeapp.data.session.SessionManager`

We need a central place to save the `accessToken` so users stay logged in after closing the app.

```kotlin
class SessionManager(context: Context) {
    private val prefs = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        prefs.edit().putString("access_token", token).apply()
    }

    fun getToken(): String? {
        return prefs.getString("access_token", null)
    }

    fun clearSession() {
        prefs.edit().clear().apply()
    }
}
```

---

## 3. The "Magic" Interceptor (Automatic Token Injection)
**File**: `com.example.mycoffeeapp.data.remote.RetrofitClient`

Instead of passing the token manually to every API, we use an `OkHttpClient` Interceptor. This automatically adds `Authorization: Bearer <TOKEN>` to every outgoing request.

```kotlin
object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:8080/"
    
    // 1. Create the Interceptor
    private val authInterceptor = Interceptor { chain ->
        val token = SessionManager.getToken() // Pseudo-code
        val request = chain.request().newBuilder()
            .apply {
                token?.let { addHeader("Authorization", "Bearer $it") }
            }
            .build()
        chain.proceed(request)
    }

    // 2. Add it to OkHttp
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .build()

    // 3. Use this client for all APIs (Cart, Favorite, etc.)
    val cartRemoteApiService: CartApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient) // <--- CRITICAL
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CartApiService::class.java)
    }
}
```

---

## 4. Repository Integration
**File**: `com.example.mycoffeeapp.data.repository.AuthRepository`

When a login is successful, we must capture the token and hand it to the `SessionManager`.

```kotlin
suspend fun login(loginRequest: LoginRequest): AuthSession {
    val session = remoteAuthDataSource.login(loginRequest)
    
    // Save the token for future use
    session.accessToken?.let { 
        sessionManager.saveToken(it) 
    }
    
    return session
}
```

---

## 5. Summary of Workflow
1.  **Login**: User logs in -> Token is received.
2.  **Save**: `SessionManager` stores the token on the disk.
3.  **Inject**: `RetrofitClient` Interceptor grabs the token and sticks it into the `Cart/Favorite` headers.
4.  **Fetch**: Backend sees the token, knows the user, and returns their personal data.
