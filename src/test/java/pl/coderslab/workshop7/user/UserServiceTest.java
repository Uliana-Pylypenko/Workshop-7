package pl.coderslab.workshop7.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    private UserServiceImpl service;

    @Mock
    private UserRepository repository;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("user", "email@gmail.com", "password");
    }

    @Test
    void givenNewUser_whenRegisterUser_thenReturnUser() {
        when(repository.save(user)).thenReturn(user);

        User result = service.registerUser(user);
        assertNotNull(result);
        assertEquals(user, result);
    }

    @Test
    void givenExistingUser_whenRegisterUser_thenThrowException() {
        when(repository.findOneByEmail(user.getEmail())).thenReturn(Optional.of(user));

        assertThrows(IllegalArgumentException.class, () -> service.registerUser(user));

        verify(repository, never()).save(any(User.class));
    }

    @Test
    void givenExistingUser_whenLoginUser_thenReturnUser() {
        when(repository.findOneByUsernameAndPassword(user.getUsername(), user.getPassword())).thenReturn(Optional.of(user));

        User result = service.loginUser(user.getUsername(), user.getPassword());
        assertNotNull(result);
        assertEquals(user, result);
    }

    @Test
    void givenWrongUserNameOrPassword_whenLoginUser_thenThrowException() {
        when(repository.findOneByUsernameAndPassword(user.getUsername(), user.getPassword())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> service.loginUser(user.getUsername(), user.getPassword()));
    }


}