package pl.coderslab.workshop7.review;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.coderslab.workshop7.festival.Festival;
import pl.coderslab.workshop7.festival.FestivalService;
import pl.coderslab.workshop7.user.Helpers;
import pl.coderslab.workshop7.user.User;
import pl.coderslab.workshop7.user.UserService;

import static org.hamcrest.Matchers.equalTo;
import static pl.coderslab.workshop7.user.Helpers.generateCredentials;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ExtendWith(SpringExtension.class)
public class ReviewControllerRestAssuredTest {
    @Autowired
    private FestivalService festivalService;

    @Autowired
    private UserService userService;

    @BeforeAll
    static void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
        RestAssured.basePath = "/review";
    }

    @Test
    void whenAddFestivalReview_thenReturnReview() {
        String username = generateCredentials(5, Helpers.CredentialType.USERNAME);
        String email = generateCredentials(5, Helpers.CredentialType.EMAIL);
        User userToSave = new User(username, email, "password");
        userService.registerUser(userToSave);
        User user = userService.loginUser(username, "password");

        Festival festivalToSave = new Festival();
        String festivalName = "Festival for review controller test " + RandomStringUtils.randomAlphanumeric(5);
        festivalToSave.setName(festivalName);
        festivalService.save(festivalToSave);
        Festival festival = festivalService.findOneByName(festivalName);

        int rating = 5;
        String comment = "This is a comment";

        RestAssured.given()
                .log().all()
                .contentType(ContentType.URLENC)
                .param("userId", user.getId())
                .param("festivalId", festival.getId())
                .param("rating", rating)
                .param("comment", comment)

                .when()
                .post("/add/festival")

                .then()
                .log().all()
                .statusCode(201)
                .body("user.username", equalTo(user.getUsername()))
                .body("festival.name", equalTo(festival.getName()))
                .body("rating", equalTo(rating))
                .body("comment", equalTo(comment));
    }
}
