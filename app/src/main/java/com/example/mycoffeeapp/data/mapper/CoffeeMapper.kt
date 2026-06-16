package com.example.mycoffeeapp.data.mapper

import com.example.mycoffeeapp.data.model.CoffeeItem
import com.example.mycoffeeapp.data.model.dto.CoffeeItemDto

fun CoffeeItemDto.toDomainModel(): CoffeeItem {
    return CoffeeItem(
        id = this.id,
        categoryId = this.categoryId,
        name = this.name,
        description = this.description,
        price = this.price,
        imageUrl = this.imageUrl,
        rating = this.rating,
        reviewCount = this.reviewCount,
        isFavorite = false
    )
}