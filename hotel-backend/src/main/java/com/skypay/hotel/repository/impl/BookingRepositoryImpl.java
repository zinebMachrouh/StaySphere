package com.skypay.hotel.repository.impl;

import com.skypay.hotel.entity.Booking;
import com.skypay.hotel.repository.BookingRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Repository
public class BookingRepositoryImpl implements BookingRepository {
    private final List<Booking> bookings = new ArrayList<>();

    @Override
    public Booking save(Booking booking) {
        bookings.add(booking);
        return booking;
    }

    @Override
    public List<Booking> findAllByRoomNumber(int roomId) {
        return bookings.stream()
                .filter(b -> roomId == b.getRoomNumber())
                .toList();
    }

    @Override
    public List<Booking> findAllOrderByCreationDateDesc() {
        return bookings.stream()
                .sorted(Comparator.comparing(Booking::getCreatedAt).reversed())
                .toList();
    }
}
