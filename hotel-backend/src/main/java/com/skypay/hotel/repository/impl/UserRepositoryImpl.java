package com.skypay.hotel.repository.impl;

import com.skypay.hotel.entity.User;
import com.skypay.hotel.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final List<User> users = new ArrayList<>();

    @Override
    public User save(User user) {
        users.add(user);
        return user;
    }

    @Override
    public Optional<User> findById(int userId) {
        return users.stream()
                .filter(u -> userId == u.getUserId())
                .findFirst();
    }

    @Override
    public List<User> findAllOrderByCreationDateDesc() {
        return users.stream()
                .sorted(Comparator.comparing(User::getCreatedAt).reversed())
                .toList();
    }
}
