package com.skypay.hotel.service.impl;

import com.skypay.hotel.dto.booking.BookingRequest;
import com.skypay.hotel.dto.booking.BookingResponse;
import com.skypay.hotel.dto.booking.BookingsAndRoomsResponse;
import com.skypay.hotel.entity.Booking;
import com.skypay.hotel.entity.Room;
import com.skypay.hotel.entity.User;
import com.skypay.hotel.exception.BadRequestException;
import com.skypay.hotel.exception.ConflictException;
import com.skypay.hotel.exception.ResourceNotFoundException;
import com.skypay.hotel.mappers.BookingMapper;
import com.skypay.hotel.mappers.RoomMapper;
import com.skypay.hotel.repository.BookingRepository;
import com.skypay.hotel.repository.RoomRepository;
import com.skypay.hotel.repository.UserRepository;
import com.skypay.hotel.service.BookingService;
import lombok.RequiredArgsConstructor;
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
    public BookingResponse save(BookingRequest bookingRequest) {
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

        return bookingMapper.toDto(bookingRepository.save(booking));
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

    private void validateBooking(BookingRequest request, User user, int totalCost) {
        LocalDate checkIn = request.checkIn();
        LocalDate checkOut = request.checkOut();

        if (checkIn.isAfter(checkOut) || checkIn.isEqual(checkOut)) {
            throw new BadRequestException("Check-out date must be after check-in date");
        }

        if (roomIsBooked(request.roomNumber(), checkIn, checkOut))
            throw new ConflictException("Room is already booked within the selected dates");

        if (!user.hasBalance(totalCost))
            throw new BadRequestException("User has insufficient balance");
    }

    private boolean roomIsBooked(int roomNumber, LocalDate newCheckIn, LocalDate newCheckOut) {
        return bookingRepository.findAllByRoomNumber(roomNumber).stream()
                .anyMatch(existing -> {
                    LocalDate existingIn = existing.getCheckIn();
                    LocalDate existingOut = existing.getCheckOut();
                    return newCheckIn.isBefore(existingOut) && existingIn.isBefore(newCheckOut);
                });
    }
}
