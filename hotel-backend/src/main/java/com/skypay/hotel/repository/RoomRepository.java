package com.skypay.hotel.repository;

import com.skypay.hotel.entity.Room;
import com.skypay.hotel.enums.RoomType;

import java.util.List;
import java.util.Optional;

public interface RoomRepository {
    Room save(Room room);
    Optional<Room> findByNumber(int roomNumber);
    List<Room> findAllOrderByCreationDateDesc();
    Optional<Room> findByTypeAndPrice(RoomType roomType, int pricePerNight);
}
