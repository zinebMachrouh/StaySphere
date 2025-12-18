package com.skypay.hotel.dto.booking;

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
public class BookingResponse {
    private Integer bookingId;
    private Integer userId;
    private Integer roomNumber;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private Integer totalCost;
    private LocalDateTime createdAt;

    private RoomType roomTypeAtBooking;
    private Integer pricePerNightAtBooking;
    private Integer userBalanceAfterBooking;
}
