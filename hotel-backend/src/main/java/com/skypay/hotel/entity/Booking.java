package com.skypay.hotel.entity;

import com.skypay.hotel.enums.RoomType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    private Long bookingId;
    private Integer userId;
    private Integer roomNumber;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private Integer totalCost;
    private LocalDateTime createdAt;

    private RoomType roomTypeAtBooking;
    private Integer pricePerNightAtBooking;
    private Integer userBalanceAfterBooking;

    public int getNumberOfNights() {
        return (int) java.time.temporal.ChronoUnit.DAYS.between(checkIn, checkOut);
    }
}