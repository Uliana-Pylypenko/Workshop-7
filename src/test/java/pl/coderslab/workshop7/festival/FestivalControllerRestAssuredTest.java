package pl.coderslab.workshop7.festival;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ExtendWith(SpringExtension.class)
public class FestivalControllerRestAssuredTest {
    @Autowired
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    static void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
        RestAssured.basePath = "/festival";
    }

    @Autowired
    private FestivalService festivalService;

    private Festival festival;

    @BeforeEach
    void init() {
        festival = new Festival();
        festival.setName("Festival");
        festival.setFestivalCategory(FestivalCategory.MUSIC);
        festivalService.save(festival);
    }

    @Test
    void whenGetUpcomingFestivalsByCategory_thenReturnFestival() {
        RestAssured.given()
                .log().all()
                .contentType(ContentType.URLENC)
                .pathParam("categoryId", festival.getFestivalCategory().getId())

                .when()
                .get("/category/{categoryId}")

                .then()
                .log().all()
                .statusCode(200);
    }
}
