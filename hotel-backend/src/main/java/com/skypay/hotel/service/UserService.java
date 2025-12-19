package com.skypay.hotel.service;

import com.skypay.hotel.dto.user.UserRequest;
import com.skypay.hotel.dto.user.UserResponse;
import org.apache.coyote.BadRequestException;

import java.util.List;

public interface UserService {
    void setUser(UserRequest userRequest) throws BadRequestException;
    List<UserResponse> printAllUsers();
}
