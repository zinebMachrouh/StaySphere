package com.skypay.hotel.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Integer userId;
    private Integer balance;
    private LocalDateTime createdAt;

    public User(Integer userId, Integer balance) {
        this.userId = userId;
        this.balance = balance;
        this.createdAt = LocalDateTime.now();
    }

    public boolean hasBalance(int amount) {
        return this.balance >= amount;
    }

    public void deductBalance(int amount) {
        this.balance -= amount;
    }
}
