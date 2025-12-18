package com.skypay.hotel.dto.room;

import com.skypay.hotel.enums.RoomType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomRequest {
    @NotNull(message = "Room number is required")
    @Min(value = 1, message = "Room number must be positive")
    private Integer roomNumber;

    @NotNull(message = "Room type is required")
    private RoomType roomType;

    @NotNull(message = "Price per night is required")
    @Min(value = 0, message = "Price must be non-negative")
    private Integer pricePerNight;
}