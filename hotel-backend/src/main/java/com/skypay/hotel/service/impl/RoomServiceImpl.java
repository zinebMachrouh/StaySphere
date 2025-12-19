package com.skypay.hotel.service.impl;

import com.skypay.hotel.dto.room.RoomRequest;
import com.skypay.hotel.entity.Room;
import com.skypay.hotel.mappers.RoomMapper;
import com.skypay.hotel.repository.RoomRepository;
import com.skypay.hotel.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;

    @Override
    public void setRoom(RoomRequest roomRequest) {
        validateNoRoomWithSameFeaturesExists(roomRequest);

        roomRepository.findByNumber(roomRequest.roomNumber()).ifPresentOrElse(
                room -> {
                    room.setRoomType(roomRequest.roomType());
                    room.setRoomNumber(roomRequest.roomNumber());
                },
                () -> roomRepository.save(
                        roomMapper.toEntity(roomRequest)
                )
        );
    }

    private void validateNoRoomWithSameFeaturesExists(RoomRequest roomRequest) {
        roomRepository.findByTypeAndPrice(roomRequest.roomType(), roomRequest.pricePerNight())
                .filter(existing -> !existing.getRoomNumber().equals(roomRequest.roomNumber()))
                .ifPresent(existing -> {
                    throw new IllegalArgumentException("Room with same type and price per night already exists");
                });
    }
}
