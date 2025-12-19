package com.skypay.hotel.repository.impl;

import com.skypay.hotel.entity.Room;
import com.skypay.hotel.enums.RoomType;
import com.skypay.hotel.repository.RoomRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Repository
public class RoomRepositoryImpl implements RoomRepository {
    private final List<Room> rooms = new ArrayList<>();

    @Override
    public Room save(Room room) {
        rooms.add(room);
        return room;
    }

    @Override
    public Optional<Room> findByNumber(int roomNumber) {
        return rooms.stream()
                .filter(r -> roomNumber == r.getRoomNumber())
                .findFirst();
    }

    @Override
    public List<Room> findAllOrderByCreationDateDesc() {
        return rooms.stream()
                .sorted(Comparator.comparing(Room::getCreatedAt).reversed())
                .toList();
    }

    @Override
    public Optional<Room> findByTypeAndPrice(RoomType roomType, int pricePerNight) {
        return rooms.stream()
                .filter(r -> r.getRoomType() == roomType && r.getPricePerNight() == pricePerNight)
                .findFirst();
    }
}
