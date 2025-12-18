package com.skypay.hotel.entity;

import com.skypay.hotel.enums.RoomType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Room {
    private Integer roomNumber;
    private RoomType roomType;
    private Integer pricePerNight;
    private LocalDateTime createdAt;

    public Room(Integer roomNumber, RoomType roomType, Integer pricePerNight) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.pricePerNight = pricePerNight;
        this.createdAt = LocalDateTime.now();
    }
}