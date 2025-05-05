package pl.coderslab.workshop7.accommodation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import pl.coderslab.workshop7.festival.Festival;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
        accommodation.setPricePerDay(5.0);
        Festival festival = new Festival();
        festival.setId(1L);
        accommodation.setFestival(festival);
    }

    @Test
    void getAccommodationsByLocationTest() throws Exception {
        when(service.findByLocation("warszawa")).thenReturn(List.of(accommodation));

        mockMvc.perform(get("/accommodation/location/warszawa"))

                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].location").value(accommodation.getLocation()));
    }

    @Test
    void getAccommodationByPriceRangeTest() throws Exception {
        when(service.findByPricePerDayBetween(any(Double.class), any(Double.class))).thenReturn(List.of(accommodation));

        mockMvc.perform(get("/accommodation/price-range")
                .param("low", String.valueOf(0.0))
                .param("high", String.valueOf(10.0)))

                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].location").value(accommodation.getLocation()))
                .andExpect(jsonPath("$[0].pricePerDay").value(accommodation.getPricePerDay()));
    }

    @Test
    void whenGetAccommodationByPriceRange_thenReturnEmptyList() throws Exception {
        when(service.findByPricePerDayBetween(any(Double.class), any(Double.class))).thenReturn(List.of());

        mockMvc.perform(get("/accommodation/price-range")
                .param("low", String.valueOf(10.0))
                .param("high", String.valueOf(20.0)))

                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    @Test
    void getAccommodationByFestivalIdTest() throws Exception {
        when(service.findByFestivalId(1L)).thenReturn(List.of(accommodation));

        mockMvc.perform(get("/accommodation/festival/1"))

                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].location").value(accommodation.getLocation()))
                .andExpect(jsonPath("$[0].pricePerDay").value(accommodation.getPricePerDay()))
                .andExpect(jsonPath("$[0].festival.id").value(1L));
    }

    @Test
    void whenGetAccommodationByFestivalId_thenReturnEmptyList() throws Exception {
        when(service.findByFestivalId(2L)).thenReturn(List.of());

        mockMvc.perform(get("/accommodation/festival/2"))

                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }


}