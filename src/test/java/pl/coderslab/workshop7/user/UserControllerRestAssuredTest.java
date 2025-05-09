package pl.coderslab.workshop7.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.coderslab.workshop7.user.Helpers.*;

import java.util.Random;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static pl.coderslab.workshop7.user.Helpers.generateCredentials;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ExtendWith(SpringExtension.class)
class UserControllerRestAssuredTest {

    @Autowired
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    static void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
        RestAssured.basePath = "/user";
    }



    @Test
    void whenRegisterUser_thenReturnUser() throws Exception {
        String username = generateCredentials(5, CredentialType.USERNAME);
        String email = generateCredentials(5, CredentialType.EMAIL);
        String password = "password";
        User user = new User(username, email, password);
        given()
                .contentType(ContentType.JSON)
                .log().all()
                .body(objectMapper.writeValueAsString(user))

                .when()
                .post("/register")

                .then()
                .log().all()
                .statusCode(201)
                .body("username", equalTo(username))
                .body("email", equalTo(email))
                .body("password", equalTo(password));
    }

    @Test
    void whenLoginUser_thenReturnUser() {
        String username = "user";
        String email = "user@gmail.com";
        String password = "password";
        given()
        .contentType(ContentType.URLENC)
                .log().all()
                .param("username", username)
                .param("password", password)

                .when()
                .post("/login")

                .then()
                .log().all()
                .statusCode(200)
                .body("username", equalTo(username))
                .body("email", equalTo(email))
                .body("password", equalTo(password));
    }

}
