package com.skypay.hotel.dto.room;

import com.skypay.hotel.enums.RoomType;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record RoomResponse (
        Integer roomNumber,
        RoomType roomType,
        Integer pricePerNight,
        LocalDateTime createdAt) {
}
