package com.skypay.hotel.service;

import com.skypay.hotel.dto.room.RoomRequest;
import com.skypay.hotel.dto.room.RoomRequest.RoomRequestBuilder;
import com.skypay.hotel.dto.room.RoomResponse;
import com.skypay.hotel.dto.room.RoomResponse.RoomResponseBuilder;
import com.skypay.hotel.entity.Room;
import com.skypay.hotel.entity.Room.RoomBuilder;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RoomServiceImpl Tests")
class RoomServiceImplTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private RoomMapper roomMapper;

    @InjectMocks
    private RoomServiceImpl roomService;

    private RoomRequestBuilder roomRequest;
    private RoomBuilder room;
    private RoomResponseBuilder roomResponse;

    @BeforeEach
    void setUp() {
        roomRequest = RoomRequest.builder().roomNumber(101).roomType(RoomType.STANDARD_SUITE).pricePerNight(1000);
        room = Room.builder()
                .roomNumber(101)
                .roomType(RoomType.STANDARD_SUITE)
                .pricePerNight(1000);
        roomResponse = RoomResponse.builder()
                .roomNumber(101);
    }

    @Test
    @DisplayName("Should create a new room when one with the given number does not exist")
    void shouldCreateNewRoomWhenRoomDoesNotExist() {
        Room savedRoom = room.build();
        RoomRequest request = roomRequest.build();

        when(roomRepository.findByNumber(101)).thenReturn(Optional.empty());
        when(roomMapper.toEntity(request)).thenReturn(savedRoom);
        when(roomRepository.save(any(Room.class))).thenReturn(savedRoom);
        when(roomMapper.toDto(savedRoom)).thenReturn(roomResponse.build());

        RoomResponse result = roomService.setRoom(request);

        assertThat(result)
                .isNotNull()
                .extracting(RoomResponse::roomNumber)
                .isEqualTo(101);

        verify(roomRepository).save(any(Room.class));
    }

    @Test
    @DisplayName("Should update an existing room when one with the given number already exists")
    void shouldUpdateRoomWhenRoomAlreadyExists() {
        Room existingRoom = room.build();
        when(roomRepository.findByNumber(101)).thenReturn(Optional.of(existingRoom));
        when(roomMapper.toDto(existingRoom)).thenReturn(roomResponse.build());

        RoomResponse result = roomService.setRoom(roomRequest.build());

        assertThat(result)
                .isNotNull()
                .extracting(RoomResponse::roomNumber)
                .isEqualTo(101);

        verify(roomRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw ConflictException when a room with the same type and price already exists")
    void shouldThrowConflictExceptionWhenRoomWithSameFeaturesExists() {
        Room existingRoom = room.roomNumber(102).build();

        when(roomRepository.findByTypeAndPrice(RoomType.STANDARD_SUITE, 1000))
                .thenReturn(Optional.of(existingRoom));

        assertThatThrownBy(() -> roomService.setRoom(roomRequest.build()))
                .isInstanceOf(ConflictException.class)
                .hasMessage("Room with same type and price per night already exists");

        verify(roomRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should allow room creation when room type and price combination is unique")
    void shouldAllowRoomCreationWhenFeaturesAreUnique() {
        when(roomRepository.findByNumber(103)).thenReturn(Optional.empty());
        when(roomRepository.findByTypeAndPrice(RoomType.JUNIOR_SUITE, 2000))
                .thenReturn(Optional.empty());

        Room newRoom = room
                .roomNumber(103)
                .roomType(RoomType.JUNIOR_SUITE)
                .pricePerNight(2000)
                .build();
        RoomResponse newResponse = roomResponse.roomNumber(103).build();
        RoomRequest newRequest = roomRequest.roomNumber(103).roomType(RoomType.JUNIOR_SUITE).pricePerNight(2000).build();

        when(roomMapper.toEntity(newRequest)).thenReturn(newRoom);
        when(roomRepository.save(any(Room.class))).thenReturn(newRoom);
        when(roomMapper.toDto(newRoom)).thenReturn(newResponse);

        RoomResponse result = roomService.setRoom(newRequest);

        assertThat(result).isNotNull();
        verify(roomRepository).save(any(Room.class));
    }
}