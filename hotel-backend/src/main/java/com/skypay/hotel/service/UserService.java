package com.skypay.hotel.service;

import com.skypay.hotel.dto.user.UserRequest;
import com.skypay.hotel.dto.user.UserResponse;

import java.util.List;

public interface UserService {
    void setUser(UserRequest userRequest);
    List<UserResponse> printAllUsers();
}
