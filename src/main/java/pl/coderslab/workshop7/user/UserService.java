package pl.coderslab.workshop7.user;

import java.util.Optional;

public interface UserService {
    User registerUser(User user);

    User loginUser(String username, String password);

    User findById(Long id);
}
