package com.skypay.hotel.entity;

import com.skypay.hotel.enums.RoomType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    @Builder.Default
    private final Integer bookingId = Instant.now().getNano();
    private Integer userId;
    private Integer roomNumber;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private Integer totalCost;
    private RoomType roomTypeAtBooking;
    private Integer pricePerNightAtBooking;
    private Integer userBalanceAfterBooking;

    @Builder.Default
    private final LocalDateTime createdAt = LocalDateTime.now();
}