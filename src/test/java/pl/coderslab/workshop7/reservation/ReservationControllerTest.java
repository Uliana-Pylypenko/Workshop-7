package pl.coderslab.workshop7.reservation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReservationController.class)
class ReservationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    ReservationService reservationService;

    @Autowired
    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void createReservationTest() throws Exception {
        Long userId = 1L;
        Long accommodationId = 1L;
        LocalDate reservationStart = LocalDate.of(2020, 1, 1);
        LocalDate reservationEnd = LocalDate.of(2020, 2, 1);

        mockMvc.perform(MockMvcRequestBuilders.post("/reservation/create")
                .param("userId", String.valueOf(userId))
                .param("accommodationId", String.valueOf(accommodationId))
                .param("reservationStartString", String.valueOf(reservationStart))
                .param("reservationEndString", String.valueOf(reservationEnd)))

                .andDo(print())
                .andExpect(status().isCreated());
//                .andExpect(jsonPath("$.user.id").value(userId))
//                .andExpect(jsonPath("$.accommodation.id").value(accommodationId))
//                .andExpect(jsonPath("$.reservationStart").value(reservationStart))
//                .andExpect(jsonPath("$.reservationEnd").value(reservationEnd));

    }

}