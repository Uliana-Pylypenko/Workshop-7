package pl.coderslab.workshop7.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User registerUser(User user) {
        Optional<User> potentialExistingUser = userRepository.findOneByEmail(user.getEmail());
        if (potentialExistingUser.isPresent()) {
            throw new IllegalArgumentException("User with email " + user.getEmail() + " already exists");
        } else {
            return userRepository.save(user);
        }
    }
}
