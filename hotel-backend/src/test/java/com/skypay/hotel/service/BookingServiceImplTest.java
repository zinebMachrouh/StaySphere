package com.skypay.hotel.service;

import com.skypay.hotel.dto.booking.BookingRequest;
import com.skypay.hotel.dto.booking.BookingRequest.BookingRequestBuilder;
import com.skypay.hotel.dto.booking.BookingResponse;
import com.skypay.hotel.dto.booking.BookingResponse.BookingResponseBuilder;
import com.skypay.hotel.entity.Booking;
import com.skypay.hotel.entity.Booking.BookingBuilder;
import com.skypay.hotel.entity.Room;
import com.skypay.hotel.entity.Room.RoomBuilder;
import com.skypay.hotel.entity.User;
import com.skypay.hotel.entity.User.UserBuilder;
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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("BookingServiceImpl Tests")
class BookingServiceImplTest {

    private static final LocalDate TODAY = LocalDate.now();

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

    private UserBuilder user;
    private RoomBuilder room;
    private BookingRequestBuilder bookingRequest;
    private BookingBuilder booking;
    private BookingResponseBuilder bookingResponse;

    @BeforeEach
    void setUp() {
        user = User.builder().userId(1).balance(10000);
        room = Room.builder()
                .roomNumber(101)
                .roomType(RoomType.STANDARD_SUITE)
                .pricePerNight(1000);
        bookingRequest = BookingRequest.builder().userId(1).roomNumber(101).checkIn(TODAY.plusDays(1)).checkOut(TODAY.plusDays(3));
        booking = Booking.builder().bookingId(1);
        bookingResponse = BookingResponse.builder().bookingId(1);
    }

    @Test
    @DisplayName("Should save booking successfully when all validations pass")
    void shouldSaveBookingWhenRequestIsValid() {
        Booking savedBooking = booking.build();

        when(userRepository.findById(1)).thenReturn(Optional.of(user.build()));
        when(roomRepository.findByNumber(101)).thenReturn(Optional.of(room.build()));
        when(bookingRepository.findAllByRoomNumber(101)).thenReturn(Collections.emptyList());
        when(bookingRepository.save(any(Booking.class))).thenReturn(savedBooking);
        when(bookingMapper.toDto(savedBooking)).thenReturn(bookingResponse.build());

        BookingResponse result = bookingService.save(bookingRequest.build());

        assertThat(result)
                .isNotNull()
                .extracting(BookingResponse::bookingId)
                .isEqualTo(1);

        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when user does not exist")
    void shouldThrowResourceNotFoundExceptionWhenUserDoesNotExist() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookingService.save(bookingRequest.build()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("User does not exist");

        verify(roomRepository, never()).findByNumber(anyInt());
        verify(bookingRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when room does not exist")
    void shouldThrowResourceNotFoundExceptionWhenRoomDoesNotExist() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user.build()));
        when(roomRepository.findByNumber(101)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookingService.save(bookingRequest.build()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Room does not exist");

        verify(bookingRepository, never()).findAllByRoomNumber(anyInt());
        verify(bookingRepository, never()).save(any());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 5})
    @DisplayName("Should throw BadRequestException when checkout date is before checkin date")
    void shouldThrowBadRequestExceptionWhenCheckOutIsBeforeCheckIn(int daysInThePast) {
        LocalDate checkIn = TODAY.plusDays(5);
        LocalDate checkOut = checkIn.minusDays(daysInThePast);
        BookingRequest invalidBookingRequest = bookingRequest.checkOut(checkOut).checkIn(checkIn).build();

        when(userRepository.findById(1)).thenReturn(Optional.of(user.build()));
        when(roomRepository.findByNumber(101)).thenReturn(Optional.of(room.build()));

        assertThatThrownBy(() -> bookingService.save(invalidBookingRequest))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Check-out date must be after check-in date");

        verify(bookingRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw ConflictException when room is already booked for selected dates")
    void shouldThrowConflictExceptionWhenRoomIsAlreadyBooked() {
        Booking existingBooking = booking
                .checkIn(TODAY)
                .checkOut(TODAY.plusDays(2))
                .build();

        when(userRepository.findById(1)).thenReturn(Optional.of(user.build()));
        when(roomRepository.findByNumber(101)).thenReturn(Optional.of(room.build()));
        when(bookingRepository.findAllByRoomNumber(101))
                .thenReturn(List.of(existingBooking));

        assertThatThrownBy(() -> bookingService.save(bookingRequest.build()))
                .isInstanceOf(ConflictException.class)
                .hasMessage("Room is already booked within the selected dates");

        verify(bookingRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw ConflictException when booking dates partially overlap with existing booking")
    void shouldThrowConflictExceptionWhenDatesPartiallyOverlap() {
        Booking existingBooking = booking
                .checkIn(TODAY.plusDays(2))
                .checkOut(TODAY.plusDays(5))
                .build();

        when(userRepository.findById(1)).thenReturn(Optional.of(user.build()));
        when(roomRepository.findByNumber(101)).thenReturn(Optional.of(room.build()));
        when(bookingRepository.findAllByRoomNumber(101))
                .thenReturn(List.of(existingBooking));

        assertThatThrownBy(() -> bookingService.save(bookingRequest.build()))
                .isInstanceOf(ConflictException.class)
                .hasMessage("Room is already booked within the selected dates");

        verify(bookingRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw BadRequestException when user has insufficient balance")
    void shouldThrowBadRequestExceptionWhenUserHasInsufficientBalance() {
        User poorUser = user.balance(100).build();

        when(userRepository.findById(1)).thenReturn(Optional.of(poorUser));
        when(roomRepository.findByNumber(101)).thenReturn(Optional.of(room.build()));

        assertThatThrownBy(() -> bookingService.save(bookingRequest.build()))
                .isInstanceOf(BadRequestException.class);

        verify(bookingRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should successfully book room when sufficient balance available")
    void shouldSuccessfullyBookRoomWithSufficientBalance() {
        User wealthyUser = user.balance(50000).build();
        Booking savedBooking = booking.build();

        when(userRepository.findById(1)).thenReturn(Optional.of(wealthyUser));
        when(roomRepository.findByNumber(101)).thenReturn(Optional.of(room.build()));
        when(bookingRepository.findAllByRoomNumber(101)).thenReturn(Collections.emptyList());
        when(bookingRepository.save(any(Booking.class))).thenReturn(savedBooking);
        when(bookingMapper.toDto(savedBooking)).thenReturn(bookingResponse.build());

        BookingResponse result = bookingService.save(bookingRequest.build());

        assertThat(result).isNotNull();
        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    @DisplayName("Should allow booking when no existing bookings conflict")
    void shouldAllowBookingWhenNoConflictingDates() {
        Booking futureBooking = booking
                .checkIn(TODAY.plusDays(5))
                .checkOut(TODAY.plusDays(7))
                .build();
        Booking savedBooking = booking.build();


        when(userRepository.findById(1)).thenReturn(Optional.of(user.build()));
        when(roomRepository.findByNumber(101)).thenReturn(Optional.of(room.build()));
        when(bookingRepository.findAllByRoomNumber(101))
                .thenReturn(List.of(futureBooking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(savedBooking);
        when(bookingMapper.toDto(savedBooking)).thenReturn(bookingResponse.build());

        BookingResponse result = bookingService.save(bookingRequest.build());

        assertThat(result).isNotNull();
        verify(bookingRepository).save(any(Booking.class));
    }
}
