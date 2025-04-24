package pl.coderslab.workshop7.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

public interface UserService {
    User registerUser(User user);

    User loginUser(String username, String password);
}
