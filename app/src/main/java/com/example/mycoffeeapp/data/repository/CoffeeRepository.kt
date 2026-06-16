package com.example.mycoffeeapp.data.repository

import androidx.compose.ui.unit.Constraints
import com.example.mycoffeeapp.constants.Constants
import com.example.mycoffeeapp.data.model.dto.CoffeeItemDto
import com.example.mycoffeeapp.data.remote.CoffeeApiService

class CoffeeRepository(private val apiService: CoffeeApiService){
    suspend fun fetchAllCoffees() : Result<List<CoffeeItemDto>> {
        return  try {
            if(Constants.USE_BACKEND){
            val response = apiService.getAllCoffees()
            Result.success(response)
            }else{
                val response = getDemoCoffees()
                Result.success(response)
            }

        }catch (e : Exception){
            if(!Constants.USE_BACKEND){
                val response = getDemoCoffees()
                Result.success(response)
            }else{
            Result.failure(e);
            }
        }
    }

    suspend fun searchCoffees(query: String): Result<List<CoffeeItemDto>> {
        val normalizedQuery = query.trim()
        if (normalizedQuery.isBlank()) {
            return fetchAllCoffees()
        }

        return try {
            if (Constants.USE_BACKEND) {
                val response = apiService.searchCoffees(normalizedQuery)
                Result.success(response)
            } else {
                val response = coffeeItemList.filter {
                    it.name.contains(normalizedQuery, ignoreCase = true) ||
                        it.name.equals(normalizedQuery, ignoreCase = true)
                }.map { item ->
                    CoffeeItemDto(
                        id = item.id,
                        categoryId = item.categoryId,
                        name = item.name,
                        description = item.description,
                        price = item.price,
                        imageUrl = item.imageUrl,
                        rating = item.rating,
                        reviewCount = item.reviewCount
                    )
                }
                Result.success(response)
            }
        } catch (e: Exception) {
            if (!Constants.USE_BACKEND) {
                val response = coffeeItemList.filter {
                    it.name.contains(normalizedQuery, ignoreCase = true) ||
                        it.name.equals(normalizedQuery, ignoreCase = true)
                }.map { item ->
                    CoffeeItemDto(
                        id = item.id,
                        categoryId = item.categoryId,
                        name = item.name,
                        description = item.description,
                        price = item.price,
                        imageUrl = item.imageUrl,
                        rating = item.rating,
                        reviewCount = item.reviewCount
                    )
                }
                Result.success(response)
            } else {
                Result.failure(e)
            }
        }
    }

    private fun getDemoCoffees(): List<CoffeeItemDto> {
        // Maps your existing coffeeItemList to DTOs for the ViewModel
        return coffeeItemList.map { item ->
            CoffeeItemDto(
                id = item.id,
                name = item.name,
                categoryId = item.categoryId,
                description = item.description,
                price = item.price,
                imageUrl = item.imageUrl,
                rating = item.rating,
                reviewCount = item.reviewCount
            )
        }
    }
}
