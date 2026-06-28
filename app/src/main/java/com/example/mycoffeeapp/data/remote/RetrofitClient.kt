package com.example.mycoffeeapp.data.remote

import com.example.mycoffeeapp.data.local.SessionManager
import com.example.mycoffeeapp.data.remote.auth.AuthApiService
import com.example.mycoffeeapp.data.remote.cart.CartApiService
import com.example.mycoffeeapp.data.remote.favorite.FavoriteApiService
import com.example.mycoffeeapp.data.remote.profile.ProfileApiService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient { // determine that only one networking clint exit throughout the lifetime of the application, for memory and conn. res conservation
    private const val BASE_URL = "http://192.168.43.140:8080/"

    private lateinit var sessionManager: SessionManager

    fun init(manager: SessionManager) {
        this.sessionManager = manager
    }

    private val authInterceptor = Interceptor { chain ->
        val token = if (::sessionManager.isInitialized) sessionManager.getAccessToken() else null
        val requestBuilder = chain.request().newBuilder()
        if (!token.isNullOrBlank()) {
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }
        chain.proceed(requestBuilder.build())
    }


    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder().addInterceptor(authInterceptor).build()
    }
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
/*
    private fun retrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create()) // Convert JSON to kotlin object
            .build()
    }
*/
    /*
    val authApiService: AuthApiService by lazy {
        retrofit().create(AuthApiService::class.java)
    }
    val favoriteApiService: FavoriteApiService by lazy {
        retrofit().create(FavoriteApiService::class.java)
    }

    val profileApiService: ProfileApiService by lazy {
        retrofit().create(ProfileApiService::class.java)
    }

    val apiService: CoffeeApiService by lazy {
        retrofit().create(CoffeeApiService::class.java)
    }
    val cartRemoteApiService: CartApiService by lazy {
        retrofit().create(CartApiService::class.java)
    }
    */

    val authApiService: AuthApiService by lazy {
        retrofit.create(AuthApiService::class.java)
    }
    val favoriteApiService: FavoriteApiService by lazy {
        retrofit.create(FavoriteApiService::class.java)
    }
    val profileApiService: ProfileApiService by lazy {
        retrofit.create(ProfileApiService::class.java)
    }
    val apiService: CoffeeApiService by lazy {
        retrofit.create(CoffeeApiService::class.java)
    }
    val cartRemoteApiService: CartApiService by lazy {
        retrofit.create(CartApiService::class.java)
    }
}
