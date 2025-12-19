package com.skypay.hotel.service.impl;

import com.skypay.hotel.dto.user.UserRequest;
import com.skypay.hotel.dto.user.UserResponse;
import com.skypay.hotel.entity.User;
import com.skypay.hotel.mappers.UserMapper;
import com.skypay.hotel.repository.UserRepository;
import com.skypay.hotel.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final UserMapper mapper;

    @Override
    public UserResponse setUser(UserRequest userRequest) {
        User user = repository.findById(userRequest.userId())
                .map(u -> {
                    u.setBalance(userRequest.balance());
                    return u;
                })
                .orElseGet(() -> repository.save(mapper.toEntity(userRequest)));
        return mapper.toDto(user);
    }

    @Override
    public List<UserResponse> printAllUsers() {
        return mapper.toDtos(repository.findAllOrderByCreationDateDesc());
    }
}
