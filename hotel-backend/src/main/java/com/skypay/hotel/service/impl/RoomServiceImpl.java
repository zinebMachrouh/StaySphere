package com.skypay.hotel.service.impl;

import com.skypay.hotel.dto.room.RoomRequest;
import com.skypay.hotel.dto.room.RoomResponse;
import com.skypay.hotel.entity.Room;
import com.skypay.hotel.exception.ConflictException;
import com.skypay.hotel.mappers.RoomMapper;
import com.skypay.hotel.repository.RoomRepository;
import com.skypay.hotel.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomRepository repository;
    private final RoomMapper mapper;

    @Override
    public RoomResponse setRoom(RoomRequest roomRequest) {
        validateNoRoomWithSameFeaturesExists(roomRequest);

        Room room = repository.findByNumber(roomRequest.roomNumber())
                .map(r -> {
                    r.setRoomType(roomRequest.roomType());
                    r.setPricePerNight(roomRequest.pricePerNight());
                    return r;
                }).orElseGet(() -> repository.save(mapper.toEntity(roomRequest)));
        return mapper.toDto(room);
    }

    private void validateNoRoomWithSameFeaturesExists(RoomRequest request) {
        repository.findByTypeAndPrice(request.roomType(), request.pricePerNight())
                .filter(existing -> !existing.getRoomNumber().equals(request.roomNumber()))
                .ifPresent(existing -> {
                    throw new ConflictException("Room with same type and price per night already exists");
                });
    }
}
