package com.skypay.hotel.service;

import com.skypay.hotel.dto.BookingsAndRoomsResponse;
import com.skypay.hotel.dto.booking.BookingRequest;

public interface BookingService {
    void save(BookingRequest bookingRequest);
    BookingsAndRoomsResponse printAll();
}
