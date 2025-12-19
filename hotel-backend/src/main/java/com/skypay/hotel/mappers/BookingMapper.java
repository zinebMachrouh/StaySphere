package com.skypay.hotel.mappers;

import com.skypay.hotel.dto.booking.BookingRequest;
import com.skypay.hotel.dto.booking.BookingResponse;
import com.skypay.hotel.entity.Booking;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookingMapper {
    Booking toEntity(BookingRequest request);
    BookingResponse toDto(Booking entity);
    List<BookingResponse> toDtos(List<Booking> entities);
}
