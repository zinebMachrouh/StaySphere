package com.skypay.hotel.repository;

import com.skypay.hotel.entity.Booking;

import java.util.List;
import java.util.Optional;

public interface BookingRepository {
    void save(Booking booking);
    Optional<Booking> findById(int bookingId);
    Optional<Booking> findByRoomNumber(int roomId);
    List<Booking> findAll();
    List<Booking> findAllOrderByCreationDateDesc();
}
