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
        Optional<User> potentiallyExistingUser = userRepository.findOneByEmail(user.getEmail());
        if (potentiallyExistingUser.isPresent()) {
            throw new IllegalArgumentException("User with email " + user.getEmail() + " already exists");
        } else {
            return userRepository.save(user);
        }
    }

    @Override
    public User loginUser(String username, String password) {
        Optional<User> userOptional = userRepository.findOneByUsernameAndPassword(username, password);
        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            throw new IllegalArgumentException("Invalid username/email or password");
        }
    }
}
