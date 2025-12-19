package com.skypay.hotel.service.impl;

import com.skypay.hotel.dto.BookingsAndRoomsResponse;
import com.skypay.hotel.dto.booking.BookingRequest;
import com.skypay.hotel.entity.Booking;
import com.skypay.hotel.entity.Room;
import com.skypay.hotel.entity.User;
import com.skypay.hotel.exception.ConflictException;
import com.skypay.hotel.exception.InsufficientBalanceException;
import com.skypay.hotel.exception.ResourceNotFoundException;
import com.skypay.hotel.mappers.BookingMapper;
import com.skypay.hotel.mappers.RoomMapper;
import com.skypay.hotel.repository.BookingRepository;
import com.skypay.hotel.repository.RoomRepository;
import com.skypay.hotel.repository.UserRepository;
import com.skypay.hotel.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@RequiredArgsConstructor
@Service
public class BookingServiceImpl implements BookingService {
    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final BookingMapper bookingMapper;
    private final RoomMapper roomMapper;

    @Override
    public void save(BookingRequest bookingRequest) throws BadRequestException {
        User user = getUser(bookingRequest.userId());
        Room room = getRoom(bookingRequest.roomNumber());
        int totalCost = bookingRequest.getNumberOfNights() * room.getPricePerNight();

        validateBooking(bookingRequest, user, totalCost);

        user.deductBalance(totalCost);

        Booking booking = Booking.builder()
                .userId(bookingRequest.userId())
                .roomNumber(bookingRequest.roomNumber())
                .checkIn(bookingRequest.checkIn())
                .checkOut(bookingRequest.checkOut())
                .totalCost(totalCost)
                .roomTypeAtBooking(room.getRoomType())
                .pricePerNightAtBooking(room.getPricePerNight())
                .userBalanceAfterBooking(user.getBalance())
                .build();

        bookingRepository.save(booking);
    }

    @Override
    public BookingsAndRoomsResponse printAll() {
        return BookingsAndRoomsResponse.builder()
                .bookings(bookingMapper.toDtos(bookingRepository.findAllOrderByCreationDateDesc()))
                .rooms(roomMapper.toDtos(roomRepository.findAllOrderByCreationDateDesc()))
                .build();
    }

    private User getUser(int userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User does not exist"));
    }

    private Room getRoom(int roomNumber) {
        return roomRepository.findByNumber(roomNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Room does not exist"));
    }

    private void validateBooking(BookingRequest bookingRequest, User user, int totalCost) throws BadRequestException {
        LocalDate checkIn = bookingRequest.checkIn();
        LocalDate checkOut = bookingRequest.checkOut();

        if (checkIn == null || checkOut == null)
            throw new BadRequestException("Check-in and check-out dates are required");

        if (!checkOut.isAfter(checkIn))
            throw new BadRequestException("Check-out date must be after check-in date");

        if (bookingRequest.getNumberOfNights() <= 0)
            throw new BadRequestException("Number of nights must be positive");

        if (roomIsBooked(bookingRequest.roomNumber(), checkIn, checkOut))
            throw new ConflictException("Room is already booked for the selected dates");

        if (!user.hasBalance(totalCost))
            throw new InsufficientBalanceException("User has insufficient balance");
    }

    private Boolean roomIsBooked(int roomNumber, LocalDate newCheckIn, LocalDate newCheckOut) {
        return bookingRepository.findAll().stream()
                .filter(b -> b.getRoomNumber() != null && b.getRoomNumber().equals(roomNumber))
                .anyMatch(existing -> {
                    LocalDate existingIn = existing.getCheckIn();
                    LocalDate existingOut = existing.getCheckOut();
                    // overlap if newIn < existingOut && newOut > existingIn
                    return newCheckIn.isBefore(existingOut) && newCheckOut.isAfter(existingIn);
                });
    }
}
