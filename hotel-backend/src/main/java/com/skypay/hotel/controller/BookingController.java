package com.skypay.hotel.controller;

import com.skypay.hotel.dto.BookingsAndRoomsResponse;
import com.skypay.hotel.dto.booking.BookingRequest;
import com.skypay.hotel.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<Void> createBooking(@Valid @RequestBody BookingRequest request) {
        bookingService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<BookingsAndRoomsResponse> getAllBookingsAndRooms() {
        return ResponseEntity.ok(bookingService.printAll());
    }
}
