package com.skypay.hotel.dto;

import com.skypay.hotel.dto.booking.BookingResponse;
import com.skypay.hotel.dto.room.RoomResponse;
import lombok.Builder;

import java.util.List;

@Builder
public record BookingsAndRoomsResponse(
    List<BookingResponse> bookings,
    List<RoomResponse> rooms) {
}
