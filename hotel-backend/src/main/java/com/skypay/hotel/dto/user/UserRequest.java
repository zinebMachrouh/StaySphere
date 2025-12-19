package com.skypay.hotel.dto.user;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UserRequest (
    @NotNull(message = "User ID is required")
    @Min(value = 1, message = "User ID must be positive")
    Integer userId,

    @NotNull(message = "Balance is required")
    @Min(value = 0, message = "Balance must be non-negative")
    Integer balance) {
}