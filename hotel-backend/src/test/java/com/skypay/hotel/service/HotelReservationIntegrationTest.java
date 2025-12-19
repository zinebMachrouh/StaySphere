package com.skypay.hotel.service;

import com.skypay.hotel.dto.booking.BookingRequest;
import com.skypay.hotel.dto.booking.BookingResponse;
import com.skypay.hotel.dto.booking.BookingsAndRoomsResponse;
import com.skypay.hotel.dto.room.RoomRequest;
import com.skypay.hotel.dto.user.UserRequest;
import com.skypay.hotel.dto.user.UserResponse;
import com.skypay.hotel.enums.RoomType;
import com.skypay.hotel.exception.BadRequestException;
import com.skypay.hotel.exception.ConflictException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class HotelReservationIntegrationTest {

    @Autowired
    private RoomService roomService;

    @Autowired
    private UserService userService;

    @Autowired
    private BookingService bookingService;

    @Test
    @DisplayName("Should correctly process a full hotel reservation scenario")
    void shouldCorrectlyProcessFullReservationScenario() {
        // Step 1: Create 3 rooms
        roomService.setRoom(RoomRequest.builder().roomNumber(1).roomType(RoomType.STANDARD_SUITE).pricePerNight(1000).build());
        roomService.setRoom(RoomRequest.builder().roomNumber(2).roomType(RoomType.JUNIOR_SUITE).pricePerNight(2000).build());
        roomService.setRoom(RoomRequest.builder().roomNumber(3).roomType(RoomType.MASTER_SUITE).pricePerNight(3000).build());

        // Step 2: Create 2 users
        userService.setUser(UserRequest.builder().userId(1).balance(5000).build());
        userService.setUser(UserRequest.builder().userId(2).balance(10000).build());
        assertThat(userService.printAllUsers()).hasSize(2);

        // Step 3: User 1 fails to book Room 2 due to insufficient balance
        assertThatThrownBy(() -> bookingService.save(BookingRequest.builder().userId(1).roomNumber(2).checkIn(LocalDate.of(2026, 6, 30)).checkOut(LocalDate.of(2026, 7, 7)).build()))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("User has insufficient balance");

        // Step 4: User 1 fails to book Room 2 due to invalid dates
        assertThatThrownBy(() -> bookingService.save(BookingRequest.builder().userId(1).roomNumber(2).checkIn(LocalDate.of(2026, 7, 7)).checkOut(LocalDate.of(2026, 6, 30)).build()))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Check-out date must be after check-in date");

        // Step 5: User 1 successfully books Room 1
        BookingResponse user1Booking = bookingService.save(BookingRequest.builder().userId(1).roomNumber(1).checkIn(LocalDate.of(2026, 7, 7)).checkOut(LocalDate.of(2026, 7, 8)).build());
        assertThat(user1Booking.totalCost()).isEqualTo(1000);
        assertThat(user1Booking.userBalanceAfterBooking()).isEqualTo(4000);

        // Step 6: User 2 fails to book Room 1 due to a conflict
        assertThatThrownBy(() -> bookingService.save(BookingRequest.builder().userId(2).roomNumber(1).checkIn(LocalDate.of(2026, 7, 7)).checkOut(LocalDate.of(2026, 7, 9)).build()))
                .isInstanceOf(ConflictException.class)
                .hasMessage("Room is already booked within the selected dates");

        // Step 7: User 2 successfully books Room 3
        BookingResponse user2Booking = bookingService.save(BookingRequest.builder().userId(2).roomNumber(3).checkIn(LocalDate.of(2026, 7, 7)).checkOut(LocalDate.of(2026, 7, 8)).build());
        assertThat(user2Booking.totalCost()).isEqualTo(3000);
        assertThat(user2Booking.userBalanceAfterBooking()).isEqualTo(7000);

        // Step 8: Update Room 1's price and type
        roomService.setRoom(RoomRequest.builder().roomNumber(1).roomType(RoomType.MASTER_SUITE).pricePerNight(10000).build());

        // Step 9: Final Assertions
        BookingsAndRoomsResponse finalState = bookingService.printAll();
        assertThat(finalState.bookings()).hasSize(2);
        assertThat(finalState.rooms()).hasSize(3);

        // Verify User 1's booking details were not affected by the room update
        assertThat(finalState.bookings())
                .filteredOn(b -> b.userId() == 1)
                .singleElement()
                .satisfies(b -> {
                    assertThat(b.pricePerNightAtBooking()).isEqualTo(1000);
                    assertThat(b.roomTypeAtBooking()).isEqualTo(RoomType.STANDARD_SUITE);
                });

        // Verify the final state of users
        List<UserResponse> finalUsers = userService.printAllUsers();
        assertThat(finalUsers)
                .hasSize(2)
                .extracting(UserResponse::userId, UserResponse::balance)
                .containsExactlyInAnyOrder(
                        tuple(1, 4000),
                        tuple(2, 7000)
                );

        System.out.println("--- Final printAll() output ---");
        System.out.println(finalState);
        System.out.println("--- Final printAllUsers() output ---");
        System.out.println(finalUsers);
    }
}
