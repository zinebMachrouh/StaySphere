package com.skypay.hotel.mappers;

import com.skypay.hotel.dto.room.RoomRequest;
import com.skypay.hotel.dto.room.RoomResponse;
import com.skypay.hotel.entity.Room;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoomMapper {
    Room toEntity(RoomRequest request);
    RoomResponse toDto(Room entity);
    List<RoomResponse> toDtos(List<Room> entities);
}
