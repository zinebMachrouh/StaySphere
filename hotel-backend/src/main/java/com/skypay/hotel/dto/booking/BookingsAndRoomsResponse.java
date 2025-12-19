package com.skypay.hotel.dto.booking;

import com.skypay.hotel.dto.room.RoomResponse;
import lombok.Builder;

import java.util.List;

@Builder
public record BookingsAndRoomsResponse(
    List<BookingResponse> bookings,
    List<RoomResponse> rooms) {
}
