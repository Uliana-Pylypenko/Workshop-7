package pl.coderslab.workshop7.user;

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

    @Test
    void findByEmailTest() {
        User user = new User();
        user.setEmail("test@gmail.com");
        entityManager.persist(user);

        Optional<User> result = userRepository.findOneByEmail("test@gmail.com");

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    void findByUsernameAndPasswordTest() {
        User user = new User();
        user.setUsername("user");
        user.setPassword("password");
        entityManager.persist(user);

        Optional<User> result = userRepository.findOneByUsernameAndPassword("user", "password");
        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

}