package com.skypay.hotel.controller;

import com.skypay.hotel.dto.room.RoomRequest;
import com.skypay.hotel.dto.room.RoomResponse;
import com.skypay.hotel.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RoomResponse createOrUpdateRoom(@Valid @RequestBody RoomRequest request) {
        return roomService.setRoom(request);
    }
}

