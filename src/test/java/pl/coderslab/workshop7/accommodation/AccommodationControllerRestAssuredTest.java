package pl.coderslab.workshop7.accommodation;

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
import pl.coderslab.workshop7.festival.Festival;
import pl.coderslab.workshop7.festival.FestivalService;

import static org.hamcrest.core.IsEqual.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ExtendWith(SpringExtension.class)
class AccommodationControllerRestAssuredTest {
    @Autowired
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    static void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
        RestAssured.basePath = "/accommodation";
    }

    @Autowired
    private AccommodationService accommodationService;

    @Autowired
    private FestivalService festivalService;

    private Accommodation accommodation;


    @BeforeEach
    void init() {
        accommodation = new Accommodation();
        accommodation.setLocation("Warszawa");
        accommodation.setPricePerDay(5.0);
        Festival festival = new Festival();
        festival.setName("Festival");
        accommodation.setFestival(festival);

        festivalService.save(festival);
        accommodationService.save(accommodation);
    }

    @Test
    void whenGetAccommodationByPriceRange_thenReturnAccommodation() {
        RestAssured.given()
                .contentType(ContentType.URLENC)
                .log().all()
                .param("low", 0.0)
                .param("high", 10.0)

                .when()
                .log().all()
                .get("/price-range")
                .then()

                .statusCode(200)
                .body("[0].location", equalTo(accommodation.getLocation()))
                .body("[0].pricePerDay", equalTo((float) accommodation.getPricePerDay()));
    }
}
