package com.skypay.hotel.dto.room;

import com.skypay.hotel.enums.RoomType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record RoomRequest (
    @NotNull(message = "Room number is required")
    @Min(value = 1, message = "Room number must be positive")
    Integer roomNumber,

    @NotNull(message = "Room type is required")
    RoomType roomType,

    @NotNull(message = "Price per night is required")
    @Min(value = 0, message = "Price must be non-negative")
    Integer pricePerNight) {
}