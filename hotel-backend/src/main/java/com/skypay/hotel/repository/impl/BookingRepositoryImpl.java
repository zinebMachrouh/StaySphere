package com.skypay.hotel.repository.impl;

import com.skypay.hotel.entity.Booking;
import com.skypay.hotel.repository.BookingRepository;
import jdk.jfr.Registered;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Registered
public class BookingRepositoryImpl implements BookingRepository {
    private final List<Booking> bookings = new ArrayList<>();

    @Override
    public void save(Booking booking) {
        bookings.add(booking);
    }

    @Override
    public Optional<Booking> findById(int bookingId) {
        return bookings.stream()
                .filter(b -> bookingId == b.getBookingId())
                .findFirst();
    }

    @Override
    public Optional<Booking> findByRoomNumber(int roomId) {
        return bookings.stream()
                .filter(b -> roomId == b.getRoomNumber())
                .findFirst();
    }

    @Override
    public List<Booking> findAll() {
        return bookings;
    }

    @Override
    public List<Booking> findAllOrderByCreationDateDesc() {
        return bookings.stream()
                .sorted(Comparator.comparing(Booking::getCreatedAt).reversed())
                .toList();
    }
}
