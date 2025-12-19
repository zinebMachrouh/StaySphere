package com.skypay.hotel.mappers;

import com.skypay.hotel.dto.room.RoomRequest;
import com.skypay.hotel.dto.room.RoomResponse;
import com.skypay.hotel.dto.user.UserRequest;
import com.skypay.hotel.dto.user.UserResponse;
import com.skypay.hotel.entity.Room;
import com.skypay.hotel.entity.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserRequest request);
    UserResponse toDto(User entity);
    List<UserResponse> toDtos(List<User> entities);
}
