package com.skypay.hotel.dto.user;

import java.time.LocalDateTime;

public record UserResponse (
    Integer userId,
    Integer balance,
    LocalDateTime createdAt) {
}
