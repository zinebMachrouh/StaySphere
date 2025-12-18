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

    @Builder.Default
    private final LocalDateTime createdAt = LocalDateTime.now();

    public boolean hasBalance(int amount) {
        return this.balance >= amount;
    }

    public void deductBalance(int amount) {
        this.balance -= amount;
    }
}
