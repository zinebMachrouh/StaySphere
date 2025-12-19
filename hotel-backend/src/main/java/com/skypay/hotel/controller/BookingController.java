package com.skypay.hotel.controller;

import com.skypay.hotel.dto.booking.BookingRequest;
import com.skypay.hotel.dto.booking.BookingResponse;
import com.skypay.hotel.dto.booking.BookingsAndRoomsResponse;
import com.skypay.hotel.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingResponse createBooking(@Valid @RequestBody BookingRequest request) {
        return bookingService.save(request);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public BookingsAndRoomsResponse getAllBookingsAndRooms() {
        return bookingService.printAll();
    }
}
