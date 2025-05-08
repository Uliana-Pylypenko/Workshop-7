package pl.coderslab.workshop7.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("user", "user@gmail.com", "password");
        entityManager.persist(user);
    }

    @Test
    void whenFindByEmail_thenReturnOptionalUser() {
        Optional<User> result = userRepository.findOneByEmail("user@gmail.com");

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    void whenFindByNonExistingEmail_thenReturnEmptyOptional() {
        Optional<User> result = userRepository.findOneByEmail("user2@gmail.com");

        assertFalse(result.isPresent());
    }

    @Test
    void whenFindByUsernameAndPassword_thenReturnOptionalUser() {
        Optional<User> result = userRepository.findOneByUsernameAndPassword("user", "password");
        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    void whenFindByNonExistingUsernameAndPassword_thenReturnEmptyOptional() {
        Optional<User> result1 = userRepository.findOneByUsernameAndPassword("user", "password2");
        Optional<User> result2 = userRepository.findOneByUsernameAndPassword("user2", "password");
        Optional<User> result3 = userRepository.findOneByUsernameAndPassword("user2", "password2");

        assertFalse(result1.isPresent());
        assertFalse(result2.isPresent());
        assertFalse(result3.isPresent());
    }

}