package pl.coderslab.workshop7.reservation;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.coderslab.workshop7.accommodation.Accommodation;
import pl.coderslab.workshop7.accommodation.AccommodationService;
import pl.coderslab.workshop7.user.Helpers;
import pl.coderslab.workshop7.user.User;
import pl.coderslab.workshop7.user.UserService;

import java.time.LocalDate;

import static pl.coderslab.workshop7.user.Helpers.generateCredentials;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ExtendWith(SpringExtension.class)
class ReservationControllerRestAssuredTest {
    @Autowired
    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private UserService userService;

    @Autowired
    private AccommodationService accommodationService;

    @BeforeAll
    static void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
        RestAssured.basePath = "/reservation";
    }

    @Test
    void whenCreateReservation_thenReturnReservation() {
        String username = generateCredentials(5, Helpers.CredentialType.USERNAME);
        String email = generateCredentials(5, Helpers.CredentialType.EMAIL);
        User userToSave = new User(username, email, "password");
        userService.registerUser(userToSave);
        User user = userService.loginUser(username, "password");

        Accommodation accommodationToSave = new Accommodation();
        String accommodationName = "Accommodation for reservation test " + RandomStringUtils.randomAlphabetic(5);
        accommodationToSave.setName(accommodationName);
        accommodationService.save(accommodationToSave);
        Accommodation accommodation = accommodationService.findOneByName(accommodationName);

        LocalDate reservationStart = LocalDate.of(2020, 1, 1);
        LocalDate reservationEnd = LocalDate.of(2020, 2, 1);

        RestAssured.given()
                .log().all()
                .contentType(ContentType.URLENC)
                .param("userId", user.getId())
                .param("accommodationId", accommodation.getId())
                .param("reservationStartString", String.valueOf(reservationStart))
                .param("reservationEndString", String.valueOf(reservationEnd))

                .when()
                .post("/create")

                .then()
                .log().all()
                .statusCode(201);


    }

}
