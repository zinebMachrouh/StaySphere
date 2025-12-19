package com.skypay.hotel.service;

import com.skypay.hotel.dto.BookingsAndRoomsResponse;
import com.skypay.hotel.dto.booking.BookingRequest;
import org.apache.coyote.BadRequestException;

public interface BookingService {
    void save(BookingRequest bookingRequest) throws BadRequestException;
    BookingsAndRoomsResponse printAll();
}
