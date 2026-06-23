package com.example.mycoffeeapp.data.remote

import com.example.mycoffeeapp.data.remote.auth.AuthApiService
import com.example.mycoffeeapp.data.remote.cart.CartApiService
import com.example.mycoffeeapp.data.remote.favorite.FavoriteApiService
import com.example.mycoffeeapp.data.remote.profile.ProfileApiService
import com.google.gson.Gson
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object RetrofitClient{ // determine that only one networking clint exit throughout the lifetime of the application, for memory and conn. res conservation
    private const val BASE_URL = "http://10.0.2.2:8080/"

    val authApiService: AuthApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthApiService::class.java)
    }
    val favoriteApiService: FavoriteApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) // Convert JSON to kotlin object
            .build()
            .create(FavoriteApiService::class.java)
    }

    val profileApiService : ProfileApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) // Convert JSON to kotlin object
            .build()
            .create(ProfileApiService::class.java)
    }

    val apiService : CoffeeApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) // Convert JSON to kotlin object
            .build()
            .create(CoffeeApiService::class.java)
    }
    val cartRemoteApiService : CartApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CartApiService::class.java)
    }
}
