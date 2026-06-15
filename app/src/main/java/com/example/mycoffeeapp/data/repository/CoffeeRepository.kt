package com.example.mycoffeeapp.data.repository

import com.example.mycoffeeapp.data.model.dto.CoffeeItemDto
import com.example.mycoffeeapp.data.remote.CoffeeApiService

class CoffeeRepository(private val apiService: CoffeeApiService){
    suspend fun fetchAllCoffees() : Result<List<CoffeeItemDto>> {
        return  try {
            val response = apiService.getAllCoffees()
            Result.success(response)
        }catch (e : Exception){
            Result.failure(e);
        }
    }
}