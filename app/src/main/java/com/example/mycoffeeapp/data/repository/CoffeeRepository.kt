package com.example.mycoffeeapp.data.repository

import com.example.mycoffeeapp.constants.Constants
import com.example.mycoffeeapp.data.mapper.toDomainModel
import com.example.mycoffeeapp.data.model.CoffeeItem
import com.example.mycoffeeapp.data.remote.CoffeeApiService

class CoffeeRepository(private val apiService: CoffeeApiService) {
    suspend fun fetchAllCoffees(): Result<List<CoffeeItem>> {
        return  try {
            if (Constants.USE_BACKEND) {
                val response = apiService.getAllCoffees().map { it.toDomainModel() }
                Result.success(response)
            } else {
                val response = getDemoCoffees()
                Result.success(response)
            }
        } catch (e: Exception) {
            if (!Constants.USE_BACKEND) {
                val response = getDemoCoffees()
                Result.success(response)
            } else {
                Result.failure(e)
            }
        }
    }

    suspend fun searchCoffees(query: String): Result<List<CoffeeItem>> {
        val normalizedQuery = query.trim()
        if (normalizedQuery.isBlank()) {
            return fetchAllCoffees()
        }

        return try {
            if (Constants.USE_BACKEND) {
                val response = apiService.searchCoffees(normalizedQuery).map { it.toDomainModel() }
                Result.success(response)
            } else {
                val response = coffeeItemList.filter {
                    it.name.contains(normalizedQuery, ignoreCase = true) ||
                        it.name.equals(normalizedQuery, ignoreCase = true)
                }
                Result.success(response)
            }
        } catch (e: Exception) {
            if (!Constants.USE_BACKEND) {
                val response = coffeeItemList.filter {
                    it.name.contains(normalizedQuery, ignoreCase = true) ||
                        it.name.equals(normalizedQuery, ignoreCase = true)
                }
                Result.success(response)
            } else {
                Result.failure(e)
            }
        }
    }

    private fun getDemoCoffees(): List<CoffeeItem> {
        return coffeeItemList
    }
}
