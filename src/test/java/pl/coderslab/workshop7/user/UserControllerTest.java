package pl.coderslab.workshop7.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper = new ObjectMapper();

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("username", "email@email.com", "password");
    }

    @Test
    void registerUser() throws Exception {
        user.setId(1L);
        when(userService.registerUser(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))

                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("username"));
    }

    @Test
    void loginUser() throws Exception {
        user.setId(1L);
        when(userService.loginUser(anyString(), anyString())).thenReturn(user);

        mockMvc.perform(post("/user/login")
                        .param("username", "user")
                        .param("password", "password"))

                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(user.getUsername()));
    }

    @Test
    public void testLoginUser_InvalidCredentials() throws Exception {
        when(userService.loginUser(anyString(), anyString())).thenThrow(new IllegalArgumentException("Invalid username/email or password"));

        mockMvc.perform(post("/user/login")
                        .param("username", "user")
                        .param("password", "wrongpassword"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}