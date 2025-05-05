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
    }

    @Test
    void getUpcomingFestivalsByCategory() throws Exception {
        when(service.getUpcomingFestivalsByCategory(any(FestivalCategory.class))).thenReturn(List.of(festival));

        mockMvc.perform(get("/festival/category/1"))

                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(festival.getName()))
                .andExpect(jsonPath("$[0].festivalCategory").value(festival.getFestivalCategory().toString()));
    }

    @Test
    void getFestivalsByName() throws Exception {
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
    void getFestivalsByLocation() throws Exception {
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
    void getFestivalByStartDate() throws Exception {
        when(service.findAllByStartDateBetween(any(LocalDate.class), any(LocalDate.class))).thenReturn(List.of(festival));

        mockMvc.perform(get("/festival/start-date")
                        .param("startDate", "2025-04-01")
                        .param("endDate", "2025-05-01"))

                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(festival.getName()))
                .andExpect(jsonPath("$[0].startDate").value(festival.getStartDate().toString()));

//        mockMvc.perform(get("/festival/start-date")
//                .param("startDate", "2025-04-01")
//                .param("endDate", "2025-03-01"))
//
//                .andDo(print())
//                .andExpect(status().isBadRequest());

    }

    @Test
    void getFestivalByEndDate() throws Exception {
        when(service.getFestivalById(any(Long.class))).thenReturn(festival);

        mockMvc.perform(get("/festival/details/1"))

                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(festival.getId()))
                .andExpect(jsonPath("$.name").value(festival.getName()))
                .andExpect(jsonPath("$.description").value(festival.getDescription()));
    }


}