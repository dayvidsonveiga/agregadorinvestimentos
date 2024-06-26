package br.com.agregadorinvestimentos.service;

import br.com.agregadorinvestimentos.dto.CreateUserDto;
import br.com.agregadorinvestimentos.dto.UpdateUserDto;
import br.com.agregadorinvestimentos.entity.User;
import br.com.agregadorinvestimentos.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @Captor
    private ArgumentCaptor<UUID> uuidArgumentCaptor;


    @Nested
    class createUser {

        @Test
        @DisplayName("Should create a user with success")
        void shouldCreateAUser() {
            var user = new User(UUID.randomUUID(), "username", "email@email.com", "password", Instant.now(), null);

            doReturn(user).when(userRepository).save(userArgumentCaptor.capture());

            var request = new CreateUserDto("username", "email@email.com", "123");

            UUID response = userService.createUser(request);

            assertNotNull(response);

            var userCaptured = userArgumentCaptor.getValue();

            assertEquals(request.username(), userCaptured.getUsername());
            assertEquals(request.email(), userCaptured.getEmail());
            assertEquals(request.password(), userCaptured.getPassword());
        }

        @Test
        @DisplayName("Should throw exception when error occurs")
        void shouldThrowsExceptionWhenErrorOccurs() {
            var user = new User(UUID.randomUUID(), "username", "email@email.com", "password", Instant.now(), null);

            doThrow(new RuntimeException()).when(userRepository).save(any());

            var request = new CreateUserDto("username", "email@email.com", "123");

            assertThrows(RuntimeException.class, () -> userService.createUser(request));
        }
    }

    @Nested
    class getUserById {

        @Test
        @DisplayName("Should get user by id with success optional is present")
        void shouldGetUserByIdWithSuccessWhenOptionalIsPresent() {
            var user = new User(UUID.randomUUID(), "username", "email@email.com", "password", Instant.now(), null);

            doReturn(Optional.of(user)).when(userRepository).findById(uuidArgumentCaptor.capture());

            var response = userService.getUserById(user.getUserId().toString());

            assertTrue(response.isPresent());
            assertEquals(user.getUserId(), uuidArgumentCaptor.getValue());
        }

        @Test
        @DisplayName("Should get user by id with success optional is empty")
        void shouldGetUserByIdWithSuccessWhenOptionalIsEmpty() {
            var userId = UUID.randomUUID();

            doReturn(Optional.empty()).when(userRepository).findById(uuidArgumentCaptor.capture());

            var response = userService.getUserById(userId.toString());

            assertTrue(response.isEmpty());
            assertEquals(userId, uuidArgumentCaptor.getValue());
        }
    }

    @Nested
    class listUsers {

        @Test
        @DisplayName("Should return all users with success")
        void shouldReturnAllUsersWithSuccess() {
            var user = new User(UUID.randomUUID(), "username", "email@email.com", "password", Instant.now(), null);

            doReturn(List.of(user)).when(userRepository).findAll();

            List<User> response = userService.listUsers();

            assertNotNull(response);
            assertEquals(1, response.size());
        }
    }

    @Nested
    class deleteById {

        @Test
        @DisplayName("Should delete user with success")
        void shouldDeleteUserWithSuccess() {
            doReturn(true).when(userRepository).existsById(uuidArgumentCaptor.capture());
            doNothing().when(userRepository).deleteById(uuidArgumentCaptor.capture());

            var userId = UUID.randomUUID();

            userService.deleteById(userId.toString());

            var idList = uuidArgumentCaptor.getAllValues();

            verify(userRepository, times(1)).existsById(idList.get(0));
            verify(userRepository, times(1)).deleteById(idList.get(1));
            assertEquals(userId, idList.get(0));
            assertEquals(userId, idList.get(1));
        }

        @Test
        @DisplayName("Should not delete when user not exists")
        void shouldNotDeleteUserWhenUserNotExists() {
            doReturn(false).when(userRepository).existsById(uuidArgumentCaptor.capture());

            var userId = UUID.randomUUID();

            userService.deleteById(userId.toString());

            verify(userRepository, times(1)).existsById(uuidArgumentCaptor.getValue());
            verify(userRepository, times(0)).deleteById(any());
        }
    }

    @Nested
    class updateUserById {

        @Test
        @DisplayName("Should update user by id when user exists and username and password is filled")
        void shouldUpdateUserByIdWhenUserExistsAndUsernameAndPasswordIsFilled() {
            var updateUserDto = new UpdateUserDto("newUsername", "newPassword");
            var user = new User(UUID.randomUUID(), "username", "email@email.com", "password", Instant.now(), null);
            doReturn(Optional.of(user)).when(userRepository).findById(uuidArgumentCaptor.capture());
            doReturn(user).when(userRepository).save(userArgumentCaptor.capture());

            userService.updateUserById(user.getUserId().toString(), updateUserDto);

            assertEquals(user.getUserId(), uuidArgumentCaptor.getValue());

            var userCaptured = userArgumentCaptor.getValue();

            assertEquals(updateUserDto.username(), userCaptured.getUsername());
            assertEquals(updateUserDto.password(), userCaptured.getPassword());

            verify(userRepository, times(1)).findById(uuidArgumentCaptor.getValue());
            verify(userRepository, times(1)).save(user);
        }

        @Test
        @DisplayName("Should not update user when user not exists")
        void shouldNotUpdateUserWhenUserNotExists() {
            var updateUserDto = new UpdateUserDto("newUsername", "newPassword");
            var userId = UUID.randomUUID();
            doReturn(Optional.empty()).when(userRepository).findById(uuidArgumentCaptor.capture());

            userService.updateUserById(userId.toString(), updateUserDto);

            assertEquals(userId, uuidArgumentCaptor.getValue());

            verify(userRepository, times(1)).findById(uuidArgumentCaptor.getValue());
            verify(userRepository, times(0)).save(any());
        }
    }
}