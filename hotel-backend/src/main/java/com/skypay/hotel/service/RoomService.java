package com.skypay.hotel.service;

import com.skypay.hotel.dto.room.RoomRequest;
import com.skypay.hotel.dto.room.RoomResponse;


public interface RoomService {
    RoomResponse setRoom(RoomRequest roomRequest);
}
