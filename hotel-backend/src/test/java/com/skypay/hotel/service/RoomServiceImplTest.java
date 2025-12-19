package com.skypay.hotel.service;

import com.skypay.hotel.dto.room.RoomRequest;
import com.skypay.hotel.dto.room.RoomResponse;
import com.skypay.hotel.entity.Room;
import com.skypay.hotel.enums.RoomType;
import com.skypay.hotel.exception.ConflictException;
import com.skypay.hotel.mappers.RoomMapper;
import com.skypay.hotel.repository.RoomRepository;
import com.skypay.hotel.service.impl.RoomServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoomServiceImplTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private RoomMapper roomMapper;

    @InjectMocks
    private RoomServiceImpl roomService;

    private RoomRequest roomRequest;
    private Room room;
    private RoomResponse roomResponse;

    @BeforeEach
    void setUp() {
        roomRequest = new RoomRequest(101, RoomType.STANDARD_SUITE, 1000);
        room = new Room();
        roomResponse = RoomResponse.builder().roomNumber(101).build();
    }

    @Test
    @DisplayName("Should create a new room when one with the given number does not exist")
    void shouldCreateNewRoomWhenRoomDoesNotExist() {
        when(roomRepository.findByNumber(101)).thenReturn(Optional.empty());
        when(roomRepository.save(any(Room.class))).thenReturn(room);
        when(roomMapper.toEntity(roomRequest)).thenReturn(room);
        when(roomMapper.toDto(room)).thenReturn(roomResponse);

        RoomResponse result = roomService.setRoom(roomRequest);

        assertNotNull(result);
        assertEquals(roomResponse.roomNumber(), result.roomNumber());
    }

    @Test
    @DisplayName("Should update an existing room when one with the given number already exists")
    void shouldUpdateRoomWhenRoomAlreadyExists() {
        when(roomRepository.findByNumber(101)).thenReturn(Optional.of(room));
        when(roomMapper.toDto(room)).thenReturn(roomResponse);

        RoomResponse result = roomService.setRoom(roomRequest);

        assertNotNull(result);
        assertEquals(roomResponse.roomNumber(), result.roomNumber());
    }

    @Test
    @DisplayName("Should throw ConflictException when a room with the same features already exists")
    void shouldThrowConflictExceptionWhenRoomWithSameFeaturesExists() {
        Room existingRoom = new Room();
        existingRoom.setRoomNumber(102);
        when(roomRepository.findByTypeAndPrice(RoomType.STANDARD_SUITE, 1000)).thenReturn(Optional.of(existingRoom));

        assertThrows(ConflictException.class, () -> roomService.setRoom(roomRequest));
    }
}
