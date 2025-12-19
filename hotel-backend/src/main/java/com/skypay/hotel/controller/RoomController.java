package com.skypay.hotel.controller;

import com.skypay.hotel.dto.room.RoomRequest;
import com.skypay.hotel.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;

    @PostMapping
    public ResponseEntity<Void> createOrUpdateRoom(@Valid @RequestBody RoomRequest request) {
        roomService.setRoom(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}

