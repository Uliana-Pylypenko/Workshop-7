package pl.coderslab.workshop7.user;

public class Main {
    public static void main(String[] args) {
        User user = new User();//1L, "user", "email", "password");
        user.setUsername("coderslab");
        System.out.println(user.getUsername());
        //System.out.println(user.getId());
        UserDTO userDTO = UserMapper.INSTANCE.toDTO(user);
        System.out.println(userDTO);
    }
}
