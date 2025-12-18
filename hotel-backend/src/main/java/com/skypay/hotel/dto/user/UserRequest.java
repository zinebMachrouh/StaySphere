package com.skypay.hotel.dto.user;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    @NotNull(message = "User ID is required")
    @Min(value = 1, message = "User ID must be positive")
    private Integer userId;

    @NotNull(message = "Balance is required")
    @Min(value = 0, message = "Balance must be non-negative")
    private Integer balance;
}