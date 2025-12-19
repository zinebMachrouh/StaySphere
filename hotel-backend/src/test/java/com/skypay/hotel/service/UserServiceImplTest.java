package com.skypay.hotel.service;

import com.skypay.hotel.dto.user.UserRequest;
import com.skypay.hotel.dto.user.UserResponse;
import com.skypay.hotel.entity.User;
import com.skypay.hotel.mappers.UserMapper;
import com.skypay.hotel.repository.UserRepository;
import com.skypay.hotel.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private UserRequest userRequest;
    private User user;
    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        userRequest = new UserRequest(1, 5000);
        user = new User();
        userResponse = UserResponse.builder().userId(1).balance(5000).build();
    }

    @Test
    @DisplayName("Should create a new user when one with the given ID does not exist")
    void shouldCreateNewUserWhenUserDoesNotExist() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toEntity(userRequest)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userResponse);

        UserResponse result = userService.setUser(userRequest);

        assertNotNull(result);
        assertEquals(userResponse.userId(), result.userId());
    }

    @Test
    @DisplayName("Should update an existing user when one with the given ID already exists")
    void shouldUpdateUserWhenUserAlreadyExists() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(userResponse);

        UserResponse result = userService.setUser(userRequest);

        assertNotNull(result);
        assertEquals(userResponse.userId(), result.userId());
    }

    @Test
    @DisplayName("Should return a list of all users, sorted from newest to oldest")
    void shouldReturnAllUsersSortedByCreationDate() {
        when(userRepository.findAllOrderByCreationDateDesc()).thenReturn(Collections.singletonList(user));
        when(userMapper.toDtos(Collections.singletonList(user))).thenReturn(Collections.singletonList(userResponse));

        List<UserResponse> result = userService.printAllUsers();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(userResponse.userId(), result.get(0).userId());
    }
}
