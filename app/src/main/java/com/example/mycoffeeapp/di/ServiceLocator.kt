package com.example.mycoffeeapp.di

import android.content.Context
import com.example.mycoffeeapp.data.local.SessionManager
import com.example.mycoffeeapp.data.remote.RetrofitClient
import com.example.mycoffeeapp.data.remote.auth.DemoAuthDataSource
import com.example.mycoffeeapp.data.remote.auth.RemoteAuthDataSource
import com.example.mycoffeeapp.data.remote.cart.DemoCartDataSource
import com.example.mycoffeeapp.data.remote.cart.RemoteClassDataSource
import com.example.mycoffeeapp.data.remote.favorite.DemoFavoriteDataSource
import com.example.mycoffeeapp.data.remote.favorite.RemoteFavoriteDataSource
import com.example.mycoffeeapp.data.remote.profile.DemoProfileDataSource
import com.example.mycoffeeapp.data.remote.profile.RemoteProfileDataSource
import com.example.mycoffeeapp.data.repository.AuthRepository
import com.example.mycoffeeapp.data.repository.CoffeeRepository
import com.example.mycoffeeapp.data.repository.cart.CartRepository
import com.example.mycoffeeapp.data.repository.favorite.FavoriteRepository
import com.example.mycoffeeapp.data.repository.profile.ProfileRepository
import dagger.hilt.android.qualifiers.ApplicationContext

object ServiceLocator {
    val authRepository: AuthRepository by lazy {
        AuthRepository(
            remoteAuthDataSource = RemoteAuthDataSource(RetrofitClient.authApiService),
            demoAuthDataSource = DemoAuthDataSource()
        )
    }
    val coffeeRepository: CoffeeRepository by lazy {
        CoffeeRepository(RetrofitClient.apiService)
    }

    val favoriteRepository: FavoriteRepository by lazy {
        FavoriteRepository(
            remote = RemoteFavoriteDataSource(RetrofitClient.favoriteApiService),
            demo = DemoFavoriteDataSource()
        )
    }

    val cartRepository: CartRepository by lazy {
        CartRepository(
            remote = RemoteClassDataSource(RetrofitClient.cartRemoteApiService),
            demo = DemoCartDataSource()
        )
    }
    private lateinit var applicationContext: Context

    val sessionManager: SessionManager by lazy {
        SessionManager(applicationContext)
    }

    // Call this once in MainActivity
    fun init(context: Context) {
        applicationContext = context.applicationContext
        RetrofitClient.init(sessionManager)
    }

    val profileRepository: ProfileRepository by lazy {
        ProfileRepository(
            remote = RemoteProfileDataSource(RetrofitClient.profileApiService),
            demo = DemoProfileDataSource(applicationContext)
        )
    }
}
