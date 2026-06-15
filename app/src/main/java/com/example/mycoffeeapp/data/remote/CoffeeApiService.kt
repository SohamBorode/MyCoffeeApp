package com.example.mycoffeeapp.data.remote

import com.example.mycoffeeapp.data.model.dto.CoffeeItemDto
import retrofit2.http.GET
import retrofit2.http.Path

interface CoffeeApiService {

    @GET("/api/coffee/{id}")
    suspend fun getCoffee(@Path("id") id : String) : CoffeeItemDto

    @GET("/api/coffees")
    suspend fun getAllCoffees() : List<CoffeeItemDto> // defined it suspend to tell the android that this functions is non-blocking, it tells that do not stop the main application if this is on waiting (like waiting for network request
}