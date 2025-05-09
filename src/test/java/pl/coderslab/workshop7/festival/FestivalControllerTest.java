package pl.coderslab.workshop7.festival;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FestivalController.class)
class FestivalControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FestivalService service;

    @Autowired
    private ObjectMapper objectMapper = new ObjectMapper();

    private Festival festival;

    @BeforeEach
    void setUp() {
        festival = new Festival();
        festival.setName("Festival");
        festival.setFestivalCategory(FestivalCategory.MUSIC);
        festival.setLocation("Warszawa");
        festival.setStartDate(LocalDate.of(2025, 4, 15));
        festival.setPricePerDay(7.0);
    }

    @Test
    void whenGetUpcomingFestivalsByCategory_thenReturnFestival() throws Exception {
        when(service.getUpcomingFestivalsByCategory(FestivalCategory.MUSIC)).thenReturn(List.of(festival));

        mockMvc.perform(get("/festival/category/1"))

                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(festival.getName()))
                .andExpect(jsonPath("$[0].festivalCategory").value(festival.getFestivalCategory().toString()));
    }

    @Test
    void whenGetUpcomingFestivalsByNonExistingCategory_thenReturnFestival() throws Exception {
        mockMvc.perform(get("/festival/category/10"))

                .andDo(print())
                .andExpect(status().isBadRequest());

    }

    @Test
    void whenGetFestivalsByName_thenReturnFestival() throws Exception {
        when(service.findAllByNameContainingIgnoreCase(any(String.class))).thenReturn(List.of(festival));

        mockMvc.perform(get("/festival/name/Festival"))

                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(festival.getName()));

        mockMvc.perform(get("/festival/name/fest"))

                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(festival.getName()));
    }

    @Test
    void whenGetFestivalsByLocation_thenReturnFestival() throws Exception {
        when(service.findAllByLocationContainingIgnoreCase(any(String.class))).thenReturn(List.of(festival));

        mockMvc.perform(get("/festival/location/Warszawa"))

                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(festival.getName()))
                .andExpect(jsonPath("$[0].location").value(festival.getLocation()));

        mockMvc.perform(get("/festival/location/war"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(festival.getName()))
                .andExpect(jsonPath("$[0].location").value(festival.getLocation()));
    }

    @Test
    void whenGetFestivalByStartDate_thenReturnFestival() throws Exception {
        when(service.findAllByStartDateBetween(any(LocalDate.class), any(LocalDate.class))).thenReturn(List.of(festival));

        mockMvc.perform(get("/festival/start-date")
                        .param("startDate", "2025-04-01")
                        .param("endDate", "2025-05-01"))

                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(festival.getName()))
                .andExpect(jsonPath("$[0].startDate").value(festival.getStartDate().toString()));

    }

    @Test
    void whenGetFestivalByWrongStartDate_thenThrowIllegalArgumentException() throws Exception {
        when(service.findAllByStartDateBetween(any(LocalDate.class), any(LocalDate.class))).thenThrow(new IllegalArgumentException());

        mockMvc.perform(get("/festival/start-date")
                .param("startDate", "2025-04-01")
                .param("endDate", "2025-03-01"))

                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenGetFestivalById_thenReturnFestival() throws Exception {
        when(service.getFestivalById(any(Long.class))).thenReturn(festival);

        mockMvc.perform(get("/festival/details/1"))

                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(festival.getId()))
                .andExpect(jsonPath("$.name").value(festival.getName()))
                .andExpect(jsonPath("$.description").value(festival.getDescription()));
    }

    @Test
    void whenGetFestivalByPriceRange_thenReturnFestival() throws Exception {
        when(service.findAllByPricePerDayBetween(any(Double.class), any(Double.class))).thenReturn(List.of(festival));
        double lower = 5.0;
        double higher = 10.0;

        mockMvc.perform(get("/festival/price-range")
                .param("lower", String.valueOf(lower))
                .param("higher", String.valueOf(higher)))

                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void whenGetFestivalByWrongPriceRange_thenThrowIllegalArgumentException() throws Exception {
        when(service.findAllByPricePerDayBetween(any(Double.class), any(Double.class))).thenThrow(new IllegalArgumentException());
        double lower = 10.0;
        double higher = 5.0;

        mockMvc.perform(get("/festival/price-range")
                .param("lower", String.valueOf(lower))
                .param("higher", String.valueOf(higher)))

                .andDo(print())
                .andExpect(status().isBadRequest());
    }

}