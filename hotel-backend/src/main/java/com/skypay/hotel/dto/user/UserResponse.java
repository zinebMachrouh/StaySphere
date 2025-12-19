package com.skypay.hotel.dto.user;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record UserResponse (
    Integer userId,
    Integer balance,
    LocalDateTime createdAt) {
}
