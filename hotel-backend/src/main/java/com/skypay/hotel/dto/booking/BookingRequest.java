package com.skypay.hotel.dto.booking;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Builder
public record BookingRequest(
        @NotNull(message = "User ID is required")
        Integer userId,

        @NotNull(message = "Room number is required")
        Integer roomNumber,

        @NotNull(message = "Check-in date is required")
        @FutureOrPresent(message = "Check-in date must be today or in the future")
        LocalDate checkIn,

        @NotNull(message = "Check-out date is required")
        @Future(message = "Check-out date must be in the future")
        LocalDate checkOut) {
    public int getNumberOfNights() {
        return (int) ChronoUnit.DAYS.between(checkIn, checkOut);
    }
}