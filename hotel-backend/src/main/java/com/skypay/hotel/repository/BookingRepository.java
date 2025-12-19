package com.skypay.hotel.repository;

import com.skypay.hotel.entity.Booking;

import java.util.List;

public interface BookingRepository {
    Booking save(Booking booking);
    List<Booking> findAllByRoomNumber(int roomId);
    List<Booking> findAllOrderByCreationDateDesc();
}
