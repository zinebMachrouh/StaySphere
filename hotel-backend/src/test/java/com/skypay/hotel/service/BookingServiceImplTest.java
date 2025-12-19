package com.skypay.hotel.service;

import com.skypay.hotel.dto.booking.BookingRequest;
import com.skypay.hotel.dto.booking.BookingResponse;
import com.skypay.hotel.entity.Booking;
import com.skypay.hotel.entity.Room;
import com.skypay.hotel.entity.User;
import com.skypay.hotel.enums.RoomType;
import com.skypay.hotel.exception.BadRequestException;
import com.skypay.hotel.exception.ConflictException;
import com.skypay.hotel.exception.ResourceNotFoundException;
import com.skypay.hotel.mappers.BookingMapper;
import com.skypay.hotel.repository.BookingRepository;
import com.skypay.hotel.repository.RoomRepository;
import com.skypay.hotel.repository.UserRepository;
import com.skypay.hotel.service.impl.BookingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookingMapper bookingMapper;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private User user;
    private Room room;
    private BookingRequest bookingRequest;
    private Booking booking;
    private BookingResponse bookingResponse;

    @BeforeEach
    void setUp() {
        user = User.builder().userId(1).balance(10000).build();
        room = Room.builder().roomNumber(101).roomType(RoomType.STANDARD_SUITE).pricePerNight(1000).build();
        bookingRequest = new BookingRequest(1, 101, LocalDate.now().plusDays(1), LocalDate.now().plusDays(3));
        booking = new Booking();
        bookingResponse = BookingResponse.builder().bookingId(1).build();
    }

    @Test
    @DisplayName("Should save booking successfully when the request is valid")
    void shouldSaveBookingWhenRequestIsValid() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(roomRepository.findByNumber(101)).thenReturn(Optional.of(room));
        when(bookingRepository.findAllByRoomNumber(101)).thenReturn(Collections.emptyList());
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
        when(bookingMapper.toDto(booking)).thenReturn(bookingResponse);

        BookingResponse result = bookingService.save(bookingRequest);

        assertNotNull(result);
        assertEquals(bookingResponse.bookingId(), result.bookingId());
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when the user does not exist")
    void shouldThrowResourceNotFoundExceptionWhenUserDoesNotExist() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bookingService.save(bookingRequest));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when the room does not exist")
    void shouldThrowResourceNotFoundExceptionWhenRoomDoesNotExist() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(roomRepository.findByNumber(101)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bookingService.save(bookingRequest));
    }

    @Test
    @DisplayName("Should throw ConflictException when the room is already booked for the selected dates")
    void shouldThrowConflictExceptionWhenRoomIsAlreadyBooked() {
        Booking existingBooking = new Booking();
        existingBooking.setCheckIn(LocalDate.now());
        existingBooking.setCheckOut(LocalDate.now().plusDays(2));

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(roomRepository.findByNumber(101)).thenReturn(Optional.of(room));
        when(bookingRepository.findAllByRoomNumber(101)).thenReturn(Collections.singletonList(existingBooking));

        assertThrows(ConflictException.class, () -> bookingService.save(new BookingRequest(1, 101, LocalDate.now().plusDays(1), LocalDate.now().plusDays(3))));
    }

    @Test
    @DisplayName("Should throw BadRequestException when the user has insufficient balance")
    void shouldThrowBadRequestExceptionWhenUserHasInsufficientBalance() {
        user.setBalance(100);
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(roomRepository.findByNumber(101)).thenReturn(Optional.of(room));

        assertThrows(BadRequestException.class, () -> bookingService.save(bookingRequest));
    }
}
