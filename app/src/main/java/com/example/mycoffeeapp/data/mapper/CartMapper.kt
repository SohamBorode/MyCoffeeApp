package com.example.mycoffeeapp.data.mapper

import com.example.mycoffeeapp.data.model.cart.CartItem
import com.example.mycoffeeapp.data.model.dto.CartItemDto

fun CartItemDto.toDomainModel(): CartItem {
    return CartItem(
        id = id,
        coffeeId = coffeeId,
        name = name,
        imageUrl = imageUrl,
        price = price,
        size = size,
        temperature = temperature,
        quantity = quantity,
        subtitle = subtitle
    )
}

fun CartItem.toDto(): CartItemDto {
    return CartItemDto(
        id = id,
        coffeeId = coffeeId,
        name = name,
        imageUrl = imageUrl,
        price = price,
        size = size,
        temperature = temperature,
        quantity = quantity,
        subtitle = subtitle
    )
}
