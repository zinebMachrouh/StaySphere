package com.skypay.hotel.service;

import com.skypay.hotel.dto.user.UserRequest;
import com.skypay.hotel.dto.user.UserResponse;
import com.skypay.hotel.entity.User;
import com.skypay.hotel.mappers.UserMapper;
import com.skypay.hotel.repository.UserRepository;
import com.skypay.hotel.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserServiceImpl Tests")
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
        user = User.builder().userId(1).balance(5000).build();
        userResponse = UserResponse.builder().userId(1).balance(5000).build();
    }

    @Nested
    @DisplayName("setUser() Tests")
    class SetUserTests {

        @Test
        @DisplayName("Should create a new user when one with the given ID does not exist")
        void shouldCreateNewUserWhenUserDoesNotExist() {
            when(userRepository.findById(1)).thenReturn(Optional.empty());
            when(userMapper.toEntity(userRequest)).thenReturn(user);
            when(userRepository.save(any(User.class))).thenReturn(user);
            when(userMapper.toDto(user)).thenReturn(userResponse);

            UserResponse result = userService.setUser(userRequest);

            assertThat(result)
                    .isNotNull()
                    .satisfies(r -> {
                        assertThat(r.userId()).isEqualTo(1);
                        assertThat(r.balance()).isEqualTo(5000);
                    });

            verify(userRepository).save(any(User.class));
        }

        @Test
        @DisplayName("Should update an existing user when one with the given ID already exists")
        void shouldUpdateUserWhenUserAlreadyExists() {
            when(userRepository.findById(1)).thenReturn(Optional.of(user));
            when(userMapper.toDto(user)).thenReturn(userResponse);

            UserResponse result = userService.setUser(userRequest);

            assertThat(result)
                    .isNotNull()
                    .extracting(UserResponse::userId, UserResponse::balance)
                    .containsExactly(1, 5000);

            verify(userRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("printAllUsers() Tests")
    class PrintAllUsersTests {

        @Test
        @DisplayName("Should return all users sorted by creation date descending")
        void shouldReturnAllUsersSortedByCreationDate() {
            User user2 = User.builder().userId(2).balance(10000).build();
            UserResponse response2 = UserResponse.builder().userId(2).balance(10000).build();
            List<User> users = List.of(user2, user);
            List<UserResponse> responses = List.of(response2, userResponse);

            when(userRepository.findAllOrderByCreationDateDesc()).thenReturn(users);
            when(userMapper.toDtos(users)).thenReturn(responses);

            List<UserResponse> result = userService.printAllUsers();

            assertThat(result)
                    .isNotNull()
                    .hasSize(2)
                    .extracting(UserResponse::userId)
                    .containsExactly(2, 1);
        }

        @Test
        @DisplayName("Should return empty list when no users exist")
        void shouldReturnEmptyListWhenNoUsers() {
            when(userRepository.findAllOrderByCreationDateDesc()).thenReturn(Collections.emptyList());
            when(userMapper.toDtos(Collections.emptyList())).thenReturn(Collections.emptyList());

            List<UserResponse> result = userService.printAllUsers();

            assertThat(result)
                    .isNotNull()
                    .isEmpty();

            verify(userRepository).findAllOrderByCreationDateDesc();
        }

        @Test
        @DisplayName("Should return single user as list")
        void shouldReturnSingleUserAsList() {
            when(userRepository.findAllOrderByCreationDateDesc()).thenReturn(List.of(user));
            when(userMapper.toDtos(List.of(user))).thenReturn(List.of(userResponse));

            List<UserResponse> result = userService.printAllUsers();

            assertThat(result)
                    .isNotNull()
                    .hasSize(1)
                    .extracting(UserResponse::userId)
                    .containsExactly(1);
        }

        @Test
        @DisplayName("Should maintain order when returning multiple users")
        void shouldMaintainOrderWhenReturningMultipleUsers() {
            User user2 = User.builder().userId(2).balance(3000).build();
            User user3 = User.builder().userId(3).balance(7500).build();
            UserResponse response2 = UserResponse.builder().userId(2).balance(3000).build();
            UserResponse response3 = UserResponse.builder().userId(3).balance(7500).build();
            List<User> users = List.of(user3, user2, user);
            List<UserResponse> responses = List.of(response3, response2, userResponse);

            when(userRepository.findAllOrderByCreationDateDesc()).thenReturn(users);
            when(userMapper.toDtos(users)).thenReturn(responses);

            List<UserResponse> result = userService.printAllUsers();

            assertThat(result)
                    .hasSize(3)
                    .extracting(UserResponse::userId)
                    .containsExactly(3, 2, 1);
        }
    }
}
