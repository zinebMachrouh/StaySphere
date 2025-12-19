package com.skypay.hotel.service;

import com.skypay.hotel.dto.booking.BookingRequest;
import com.skypay.hotel.dto.booking.BookingResponse;
import com.skypay.hotel.dto.booking.BookingsAndRoomsResponse;

public interface BookingService {
    BookingResponse save(BookingRequest bookingRequest);
    BookingsAndRoomsResponse printAll();
}
