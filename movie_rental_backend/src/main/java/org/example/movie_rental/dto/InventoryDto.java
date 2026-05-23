package org.example.movie_rental.dto;

public record InventoryDto(
        Integer inventoryId,
        Integer storeId,
        boolean isRented
) {}
