package com.skypay.hotel.dto.booking;

import com.skypay.hotel.enums.RoomType;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
public record BookingResponse(
        Integer bookingId,
        Integer userId,
        Integer roomNumber,
        LocalDate checkIn,
        LocalDate checkOut,
        Integer totalCost,
        LocalDateTime createdAt,
        RoomType roomTypeAtBooking,
        Integer pricePerNightAtBooking,
        Integer userBalanceAfterBooking) {
}
