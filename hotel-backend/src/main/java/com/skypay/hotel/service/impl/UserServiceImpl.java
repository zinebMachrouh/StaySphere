package com.skypay.hotel.service.impl;

import com.skypay.hotel.dto.user.UserRequest;
import com.skypay.hotel.dto.user.UserResponse;
import com.skypay.hotel.mappers.UserMapper;
import com.skypay.hotel.repository.UserRepository;
import com.skypay.hotel.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public void setUser(UserRequest userRequest) {
        userRepository.findById(userRequest.userId()).ifPresentOrElse(
                u -> u.setBalance(userRequest.balance()),
                () -> userRepository.save(userMapper.toEntity(userRequest)));
    }

    @Override
    public List<UserResponse> printAllUsers() {
        return userMapper.toDtos(userRepository.findAllOrderByCreationDateDesc());
    }
}
