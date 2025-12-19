package com.skypay.hotel.repository;

import com.skypay.hotel.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(int userId);
    List<User> findAllOrderByCreationDateDesc();
}
