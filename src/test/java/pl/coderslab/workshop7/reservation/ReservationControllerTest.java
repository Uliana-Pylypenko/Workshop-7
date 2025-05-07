package pl.coderslab.workshop7.reservation;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.coderslab.workshop7.user.User;

import javax.swing.text.html.parser.Entity;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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

    private Reservation reservation;

    @BeforeEach
    void setUp() {
        reservation = new Reservation();
        reservation.setId(1L);
        LocalDate reservationStart = LocalDate.of(2020, 1, 1);
        LocalDate reservationEnd = LocalDate.of(2020, 2, 1);
        reservation.setReservationStart(reservationStart);
        reservation.setReservationEnd(reservationEnd);
    }

    @Test
    void createReservationTest() throws Exception {
        Long userId = 1L;
        Long accommodationId = 1L;
        LocalDate reservationStart = LocalDate.of(2020, 1, 1);
        LocalDate reservationEnd = LocalDate.of(2020, 2, 1);

        Reservation reservation = new Reservation();
        reservation.setId(1L);
        reservation.setReservationStart(reservationStart);
        reservation.setReservationEnd(reservationEnd);

        when(reservationService.create(userId, accommodationId, reservationStart, reservationEnd))
                .thenReturn(reservation);

        mockMvc.perform(MockMvcRequestBuilders.post("/reservation/create")
                .param("userId", String.valueOf(userId))
                .param("accommodationId", String.valueOf(accommodationId))
                .param("reservationStartString", String.valueOf(reservationStart))
                .param("reservationEndString", String.valueOf(reservationEnd)))

                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.reservationStart").value(String.valueOf(reservationStart)))
                .andExpect(jsonPath("$.reservationEnd").value(String.valueOf(reservationEnd)));
    }

    @Test
    void createReservationCatchDateTimeParseException() throws Exception {
        Long userId = 1L;
        Long accommodationId = 1L;
        String reservationStart = "20.04.2024";
        String reservationEnd = "20.05.2025";

        mockMvc.perform(MockMvcRequestBuilders.post("/reservation/create")
                .param("userId", String.valueOf(userId))
                .param("accommodationId", String.valueOf(accommodationId))
                .param("reservationStartString", reservationStart)
                .param("reservationEndString", reservationEnd))

                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void createReservationCatchIllegalArgumentException() throws Exception {
        Long userId = 1L;
        Long accommodationId = 1L;
        LocalDate reservationStart = LocalDate.of(2025, 1, 1);
        LocalDate reservationEnd = LocalDate.of(2020, 1, 1);

        when(reservationService.create(userId, accommodationId, reservationStart, reservationEnd)).thenThrow(IllegalArgumentException.class);

        mockMvc.perform(MockMvcRequestBuilders.post("/reservation/create")
                        .param("userId", String.valueOf(userId))
                        .param("accommodationId", String.valueOf(accommodationId))
                        .param("reservationStartString", String.valueOf(reservationStart))
                        .param("reservationEndString", String.valueOf(reservationEnd)))

                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void createReservationCatchEntityNotFoundException() throws Exception {
        Long userId = 1L;
        Long accommodationId = 1L;
        LocalDate reservationStart = LocalDate.of(2025, 1, 1);
        LocalDate reservationEnd = LocalDate.of(2020, 1, 1);

        when(reservationService.create(userId, accommodationId, reservationStart, reservationEnd)).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders.post("/reservation/create")
                        .param("userId", String.valueOf(userId))
                        .param("accommodationId", String.valueOf(accommodationId))
                        .param("reservationStartString", String.valueOf(reservationStart))
                        .param("reservationEndString", String.valueOf(reservationEnd)))

                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void findAllByUserIdTest() throws Exception {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        reservation.setUser(user);

        when(reservationService.findAllByUserId(userId)).thenReturn(List.of(reservation));

        mockMvc.perform(MockMvcRequestBuilders.get("/reservation/all/1"))

                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(reservation.getId().toString()))
                .andExpect(jsonPath("$[0].user.id").value(user.getId().toString()))
                .andExpect(jsonPath("$[0].reservationStart").value(reservation.getReservationStart().toString()))
                .andExpect(jsonPath("$[0].reservationEnd").value(reservation.getReservationEnd().toString()));
    }

    @Test
    void findAllByUserIdException() throws Exception {
        when(reservationService.findAllByUserId(1L)).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/reservation/all/1"))

                .andDo(print())
                .andExpect(status().isNotFound());
    }



}