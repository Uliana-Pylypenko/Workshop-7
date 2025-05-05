package pl.coderslab.workshop7.user;

public interface UserService {
    User registerUser(User user);

    User loginUser(String username, String password);
}
