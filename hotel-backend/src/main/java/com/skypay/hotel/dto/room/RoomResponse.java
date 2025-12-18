package com.skypay.hotel.dto.room;

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
public class RoomResponse {
    private Integer roomNumber;
    private RoomType roomType;
    private Integer pricePerNight;
    private LocalDateTime createdAt;
}
