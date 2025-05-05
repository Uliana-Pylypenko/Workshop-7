package pl.coderslab.workshop7.accommodation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccommodationController.class)
class AccommodationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AccommodationService service;

    @Autowired
    private ObjectMapper objectMapper = new ObjectMapper();

    private Accommodation accommodation;

    @BeforeEach
    void setUp() {
        accommodation = new Accommodation();
        accommodation.setLocation("Warszawa");
    }

    @Test
    void getAccommodationsByLocationTest() throws Exception {
        when(service.findByLocation("Warszawa")).thenReturn(List.of(accommodation));

        mockMvc.perform(get("/accommodation/location/Warszawa"))

                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].location").value(accommodation.getLocation()));
    }


}